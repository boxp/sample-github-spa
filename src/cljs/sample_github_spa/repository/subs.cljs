(ns sample-github-spa.repository.subs
  (:require
   [re-frame.core :as re-frame]))

(re-frame/reg-sub
 ::repositories
 (fn [db]
   (-> db :repository :repositories)))
