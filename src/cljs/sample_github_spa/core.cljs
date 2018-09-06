(ns sample-github-spa.core
  (:require
   [reagent.core :as reagent]
   [re-frame.core :as re-frame]
   [sample-github-spa.events :as events]
   [sample-github-spa.views :as views]
   [sample-github-spa.config :as config]
   ))


(defn dev-setup []
  (when config/debug?
    (enable-console-print!)
    (println "dev mode")))

(defn mount-root []
  (re-frame/clear-subscription-cache!)
  (reagent/render [views/main-panel]
                  (.getElementById js/document "app")))

(defn ^:export init []
  (re-frame/dispatch-sync [::events/initialize-db])
  (dev-setup)
  (mount-root))
