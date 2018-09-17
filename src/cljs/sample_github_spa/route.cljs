(ns sample-github-spa.route
  (:require
    [re-frame.core :as re-frame]
    [sample-github-spa.util :as util]
    [sample-github-spa.events :as events]
    [secretary.core :as secretary :refer-macros [defroute]]))

;; prefixなし
(secretary/set-config! :prefix "/")

(def route-table
  {:login {:title "Login"
           :container #(resolve 'sample-github-spa.auth.container/box)
           :module-name :auth}
   :repository {:title "Repository"
                :container #(resolve 'sample-github-spa.repository.container/grid-box)
                :module-name :repository}
   :about-repository {:title "About Repository"
                      :container #(resolve 'sample-github-spa.repository.container/detail)
                      :module-name :repository}
   :activity {:title "Activity"
              :container #(resolve 'sample-github-spa.activity.container/timeline)
              :module-name :activity}
   :about-activity {:title "About Activity"
                    :container #(resolve 'sample-github-spa.activity.container/detail)
                    :module-name :activity}})

(defn- lazy-push
  [key params]
  (util/universal-load (-> route-table key :module-name) #(re-frame/dispatch-sync [::events/push key params])))

;; ルーティング定義
(defroute root-path "/" []
  (lazy-push :login {}))

(defroute repository-path "/repository" []
  (lazy-push :repository {}))

(defroute repository-detail-path "/repository/:id" [id]
  (lazy-push :about-repository {:id id}))

(defroute activity-path "/activity" []
  (lazy-push :activity {}))

(defroute activity-detail-path "/activity/:id" [id]
  (lazy-push :about-activity {:id id}))

(defroute not-found-path "*" []
  (lazy-push :login {}))
