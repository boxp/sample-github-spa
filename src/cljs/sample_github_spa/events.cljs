(ns sample-github-spa.events
  (:require
   [re-frame.core :as re-frame]
   [sample-github-spa.db :as db]
   ))

(re-frame/reg-event-db
 ::initialize-db
 (fn [_ _]
   db/default-db))
