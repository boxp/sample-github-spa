(ns sample-github-spa.auth.container
  (:require [cljs.loader :as loader]
            [reagent.core :as reagent]
            [sample-github-spa.auth.component :as component]
            [sample-github-spa.auth.events :as events]
            [re-frame.core :as re-frame]))

;; metaデータがcljs.loader/loadによって上書きされるためreagent/create-classでLifecycleを定義している
(defn box
  [params]
  (reagent/create-class
   {:component-did-mount (fn [] (re-frame/dispatch [::events/initialize]))
    :reagent-render (fn [params]
                      [component/box #(re-frame/dispatch [::events/login])])}))

(loader/set-loaded! :auth)
