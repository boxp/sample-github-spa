(ns sample-github-spa.core
  (:require
   [reagent.core :as reagent]
   [re-frame.core :as re-frame]
   [sample-github-spa.events :as events]
   [sample-github-spa.repository.list-component :as repository-list]
   [sample-github-spa.config :as config]
   ))


(defn dev-setup []
  (when config/debug?
    (enable-console-print!)
    (println "dev mode")))

(defn mount-root []
  (re-frame/clear-subscription-cache!)
  (reagent/render [repository-list/component [{:id 1
                                               :name "hoge"
                                               :owner {:avatar_url "https://dummyimage.com/64x64/000/fff"}}
                                              {:id 2
                                               :name "fuga"
                                               :owner {:avatar_url "https://dummyimage.com/64x64/000/fff"}}]]
                  (.getElementById js/document "app")))

(defn ^:export init []
  (re-frame/dispatch-sync [::events/initialize-db])
  (dev-setup)
  (mount-root))
