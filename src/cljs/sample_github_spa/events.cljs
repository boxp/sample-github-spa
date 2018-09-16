(ns sample-github-spa.events
  (:require
   [re-frame.core :as re-frame]
   [sample-github-spa.effects :as effects]
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

(re-frame/reg-event-fx
 ::flush-token
 (fn [{:keys [db]} _]
   {:db (-> db
            (assoc :token nil))
    ::effects/set-localstorage ["access-token" nil]}))

(re-frame/reg-event-fx
 ::redirect-to-auth
 (fn [{:keys [db]} _]
   {::effects/route ["/"]}))

(re-frame/reg-event-fx
 ::logout
 (fn [{:keys [db]} _]
   {:dispatch-n [[::flush-token] [::redirect-to-auth]]}))
