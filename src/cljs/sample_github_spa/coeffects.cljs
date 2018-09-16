(ns sample-github-spa.coeffects
  (:require
   [cljs.reader :as reader]
   [re-frame.core :as re-frame]
   [re-frame.db :as re-frame-db]
   [pushy.core :as pushy]))

(re-frame/reg-cofx
 ::local-store
 (fn [coeffects key]
   (assoc coeffects
          :local-store
          (reader/read-string (.getItem js/localStorage key)))))

(re-frame/reg-cofx
 ::route
 (fn [coeffects]
   (assoc coeffects
          :route
          (some->
           @re-frame-db/app-db
           :history
           pushy/get-token))))
