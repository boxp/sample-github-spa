(ns sample-github-spa.client
  (:require
   [cljs.loader :as loader]
   [pushy.core :as pushy]
   [reagent.core :as reagent]
   [re-frame.core :as re-frame]
   [sample-github-spa.events :as events]
   [sample-github-spa.component :as component]
   [sample-github-spa.route]
   [sample-github-spa.repository.component :as repository]
   [sample-github-spa.config :as config]
   [secretary.core :as secretary :refer-macros [defroute]]))

(defn dev-setup []
  (when config/debug?
    (enable-console-print!)
    (println "dev mode")))

(def history
  (pushy/pushy secretary/dispatch!
               (fn [x] (when (secretary/locate-route x) x))))

;; https://github.com/kibu-australia/pushy#routing-libraries から輸入
(defn hook-history []
  (pushy/start! history))

(defn ^:export mount-root []
  (re-frame/clear-subscription-cache!)
  (reagent/render [component/app]
                  (.getElementById js/document "app")))

(defn ^:export init []
  (re-frame/dispatch-sync [::events/initialize history])
  (dev-setup)
  (hook-history)
  (mount-root))

(set! (. js/window -onload) init)

(loader/set-loaded! :client)
