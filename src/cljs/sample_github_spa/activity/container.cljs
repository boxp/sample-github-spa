(ns sample-github-spa.activity.container
  (:require [cljs.loader :as loader]
            [sample-github-spa.activity.component :as component]))

(defn ^:export timeline
  [params]
  [component/timeline [{:id 1
                        :actor {:avatar_url "https://dummyimage.com/64x64/000/fff"}
                        :payload "hoge"}
                       {:id 2
                        :actor {:avatar_url "https://dummyimage.com/64x64/000/fff"}
                        :payload "hoge"}
                       {:id 3
                        :actor {:avatar_url "https://dummyimage.com/64x64/000/fff"}
                        :payload "hoge"}]])

(defn ^:export detail
  [params]
  [component/detail {:id 1
                     :actor {:avatar_url "https://dummyimage.com/64x64/000/fff"}
                     :payload "hoge"}])

(loader/set-loaded! :activity)
