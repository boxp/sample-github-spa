(ns sample-github-spa.activity.container
  (:require [cljs.loader :as loader]
            [reagent.core :as reagent]
            [re-frame.core :as re-frame]
            [sample-github-spa.component]
            [sample-github-spa.activity.component :as component]
            [sample-github-spa.activity.subs :as subs]
            [sample-github-spa.activity.events :as events]))

(defn ^:export timeline
  [params]
  (let [activities (re-frame/subscribe [::subs/activities])
        should-load-more? (re-frame/subscribe [::subs/should-load-more?])
        handle-load-more #(re-frame/dispatch [::events/fetch-next-own-activities])]
    (reagent/create-class
     {:component-did-mount (fn [] (re-frame/dispatch [::events/refresh-own-activities]))
      :reagent-render (fn [params]
                        [:div
                         [component/timeline {:activities @activities}]
                         [:div
                          [sample-github-spa.component/infinite-scroll {:can-show-more? @should-load-more?
                                                                        :load-fn handle-load-more}]]])})))

(defn ^:export detail
  [params]
  (let [activity (re-frame/subscribe [::subs/get-activity-by-id (:id params)])]
    [component/detail {:activity @activity}]))

(loader/set-loaded! :activity)
