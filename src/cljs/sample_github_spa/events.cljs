(ns sample-github-spa.events
  (:require
   [re-frame.core :as re-frame]
   [sample-github-spa.db :as db]))

(re-frame/reg-event-db
 ::initialize-db
 (fn [_ _]
   db/default-db))

(re-frame/reg-event-db
 ::push
 (fn [db [_ title component params]]
   (-> db
       (assoc-in [:router :title] title)
       (assoc-in [:router :component] component)
       (assoc-in [:router :params] params))))
