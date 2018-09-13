(ns sample-github-spa.events
  (:require
   [re-frame.core :as re-frame]
   [sample-github-spa.coeffects :as coeffects]
   [sample-github-spa.db :as db]))

(re-frame/reg-event-fx
 ::initialize
 [(re-frame/inject-cofx ::coeffects/local-store "access-token")]
 (fn [{:keys [local-store]} [_ history]]
   (as-> {:db db/default-db} $
     (assoc-in $ [:db :history] history)
     (if local-store (assoc-in $ [:db :token] local-store) $))))

(re-frame/reg-event-db
 ::push
 (fn [db [_ title component params]]
   (-> db
       (assoc-in [:router :title] title)
       (assoc-in [:router :component] component)
       (assoc-in [:router :params] params))))

(re-frame/reg-event-fx
 ::api-error
 (fn [{:keys [db]} [_ error]]
   {:db (assoc db :api-error error)}))
