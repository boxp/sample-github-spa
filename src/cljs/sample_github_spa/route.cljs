(ns sample-github-spa.route
  (:require
   [cljs.loader :as loader]
   [re-frame.core :as re-frame]
   [sample-github-spa.events :as events]
   [secretary.core :as secretary :refer-macros [defroute]]))

;; prefixなし
(secretary/set-config! :prefix "/")

;; ルーティング定義
(defroute root-path "/" []
  (loader/load :auth
               (fn []
                 (re-frame/dispatch [::events/push "Login" (resolve 'sample-github-spa.auth.container/box) {}]))))

(defroute repository-path "/repository" []
  (loader/load :repository
               (fn []
                 (re-frame/dispatch [::events/push "Repository" (doto (resolve 'sample-github-spa.repository.container/grid-box) (-> meta println)) {}]))))

(defroute repository-detail-path "/repository/:id" [id]
  (loader/load :repository
               (fn []
                 (re-frame/dispatch [::events/push "About Repository" (resolve 'sample-github-spa.repository.container/detail) {:id id}]))))

(defroute activity-path "/activity" []
  (loader/load :activity
               (fn []
                 (re-frame/dispatch [::events/push "Activity" (resolve 'sample-github-spa.activity.container/timeline) {}]))))

(defroute activity-detail-path "/activity/:id" [id]
  (loader/load :activity
               (fn []
                 (re-frame/dispatch [::events/push "About Activity" (resolve 'sample-github-spa.activity.container/detail) {:id id}]))))

(defroute not-found-path "*" []
  (loader/load :auth
               (fn []
                 (re-frame/dispatch [::events/push "Login" (resolve 'sample-github-spa.auth.container/box) {}]))))
