(ns sample-github-spa.activity.events
  (:require
    [re-frame.core :as re-frame]
    [ajax.core :as ajax]
    [day8.re-frame.http-fx]
    [sample-github-spa.events :as events]))

(def activities-per-page 20)

(re-frame/reg-event-fx
 ::refresh-own-activities
 (fn [{:keys [db]} _]
   {:db (-> db
            (assoc-in [:activity :per-page] activities-per-page)
            (assoc-in [:activity :page] 1))
    :dispatch [::fetch-own-activities [::on-success-refresh-own-activities]]}))

(re-frame/reg-event-fx
 ::fetch-next-own-activities
 (fn [{:keys [db]} _]
   {:dispatch [::fetch-own-activities [::on-success-fetch-next-own-activities]]}))

(re-frame/reg-event-fx
 ::fetch-own-activities
 (fn [{:keys [db]} [_ on-success]]
   {:http-xhrio {:method :get
                 :uri "https://api.github.com/notifications"
                 :params {:access_token (:token db)
                          :page (-> db :activity :page)
                          :per_page activities-per-page}
                 :request-format (ajax/url-request-format)
                 :response-format (ajax/json-response-format {:keywords? true})
                 :on-success on-success
                 :on-failure [::events/api-error]}}))

(re-frame/reg-event-db
 ::on-success-refresh-own-activities
 (fn [db [_ results]]
   (-> db
       (assoc-in [:activity :activities] results)
       (update-in [:activity :page] #(inc %)))))

(re-frame/reg-event-db
 ::on-success-fetch-next-own-activities
 (fn [db [_ results]]
   (-> db
       (update-in [:activity :activities] #(concat % results))
       (update-in [:activity :page] #(inc %)))))
