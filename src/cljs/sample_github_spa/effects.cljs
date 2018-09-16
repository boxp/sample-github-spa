(ns sample-github-spa.effects
  (:require
   [re-frame.core :as re-frame]
   [re-frame.db :as db]
   [pushy.core :as pushy]))

(re-frame/reg-fx
 ::set-localstorage
 (fn [[key item]]
   (.setItem js/localStorage key (pr-str item))))

(re-frame/reg-fx
 ::route
 (fn [[path]]
   (pushy/set-token! (:history @db/app-db) path)))
