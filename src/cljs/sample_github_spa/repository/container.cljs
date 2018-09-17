(ns sample-github-spa.repository.container
  (:require [reagent.core :as reagent]
            [sample-github-spa.component]
            [sample-github-spa.repository.component :as component]
            [sample-github-spa.repository.subs :as subs]
            [sample-github-spa.repository.events :as events]
            [sample-github-spa.util :as util]
            [re-frame.core :as re-frame]))

;; metaデータがcljs.loader/loadによって上書きされるためreagent/create-classでLifecycleを定義している
(defn ^:export grid-box
  [params]
  (let [repositories (re-frame/subscribe [::subs/repositories])
        should-load-more? (re-frame/subscribe [::subs/should-load-more?])
        handle-load-more #(re-frame/dispatch [::events/fetch-next-own-repositories])]
    (reagent/create-class
     {:component-did-mount (fn []
                             (re-frame/dispatch [::events/refresh-own-repositories]))
      :reagent-render (fn [params]
                        [:div
                         [component/grid-box {:repositories @repositories}]
                         [:div
                          [sample-github-spa.component/infinite-scroll {:can-show-more? @should-load-more?
                                                                        :load-fn handle-load-more}]]])})))

(defn ^:export detail
  [{:keys [id] :as params}]
  (let [repository (re-frame/subscribe [::subs/get-repository-by-id (js/Number id)])]
    [component/detail @repository]))

(util/universal-set-loaded! :repository)
