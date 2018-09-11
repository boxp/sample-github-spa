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
            (assoc-in [:repository :repositories] [])
            (assoc-in [:repository :page] 1))
    :dispatch [::fetch-own-repositories]}))

(re-frame/reg-event-fx
 ::fetch-own-repositories
 (fn [{:keys [db]} _]
   {:http-xhrio {:method :get
                 :uri "https://api.github.com/user/repos"
                 :params {:access_token (:token db)
                          :per_page repositories-per-page}
                 :request-format (ajax/url-request-format)
                 :response-format (ajax/json-response-format {:keywords? true})
                 :on-success [::on-success-fetch-own-repositories]
                 :on-failure [::events/api-error]}}))

(re-frame/reg-event-db
 ::on-success-fetch-own-repositories
 (fn [db [_ results]]
   (-> db
       (update-in [:repository :repositories] #(concat % results))
       (update-in [:repository :page] #(inc %)))))
