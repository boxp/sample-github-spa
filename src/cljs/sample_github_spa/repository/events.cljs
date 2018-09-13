(ns sample-github-spa.repository.events
  (:require
   [re-frame.core :as re-frame]
   [ajax.core :as ajax]
   [day8.re-frame.http-fx]
   [sample-github-spa.events :as events]))

(def repositories-per-page 20)

(re-frame/reg-event-fx
 ::refresh-own-repositories
 (fn [{:keys [db]} _]
   {:db (-> db
            (assoc-in [:repository :per-page] repositories-per-page)
            (assoc-in [:repository :page] 1))
    :dispatch [::fetch-own-repositories [::on-success-refresh-own-repositories]]}))

(re-frame/reg-event-fx
 ::fetch-next-own-repositories
 (fn [{:keys [db]} _]
   {:dispatch [::fetch-own-repositories [::on-success-fetch-next-own-repositories]]}))

(re-frame/reg-event-fx
 ::fetch-own-repositories
 (fn [{:keys [db]} [_ on-success]]
   {:http-xhrio {:method :get
                 :uri "https://api.github.com/user/repos"
                 :params {:access_token (:token db)
                          :page (-> db :repository :page)
                          :per_page repositories-per-page}
                 :request-format (ajax/url-request-format)
                 :response-format (ajax/json-response-format {:keywords? true})
                 :on-success on-success
                 :on-failure [::events/api-error]}}))

(re-frame/reg-event-db
 ::on-success-refresh-own-repositories
 (fn [db [_ results]]
   (-> db
       (assoc-in [:repository :repositories] results)
       (update-in [:repository :page] #(inc %)))))

(re-frame/reg-event-db
 ::on-success-fetch-next-own-repositories
 (fn [db [_ results]]
   (-> db
       (update-in [:repository :repositories] #(concat % results))
       (update-in [:repository :page] #(inc %)))))
