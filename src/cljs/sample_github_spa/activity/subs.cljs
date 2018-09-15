(ns sample-github-spa.activity.subs
  (:require [cljs.loader :as loader]
            [re-frame.core :as re-frame]))

(re-frame/reg-sub
  ::activities
  (fn [db]
    (-> db :activity :activities)))

(re-frame/reg-sub
  ::should-load-more?
  (fn [db]
    (zero? (mod (-> db :activity :activities count)
                (-> db :activity :per-page)))))

(re-frame/reg-sub
  ::get-activity-by-id
  (fn [db [_ id]]
    (some->> db
             :activity
             :activities
             (filter #(-> % :id (= id)))
             first)))
