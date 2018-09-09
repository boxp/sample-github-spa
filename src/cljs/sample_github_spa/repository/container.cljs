(ns sample-github-spa.repository.container
  (:require [cljs.loader :as loader]
            [sample-github-spa.repository.component :as component]))

(defn ^:export grid-box
  [params]
  [component/grid-box [{:id 1
                        :name "hoge"
                        :owner {:avatar_url "https://dummyimage.com/64x64/000/fff"}}
                       {:id 2
                        :name "fuga"
                        :owner {:avatar_url "https://dummyimage.com/64x64/000/fff"}}
                       {:id 3
                        :name "fuga"
                        :owner {:avatar_url "https://dummyimage.com/64x64/000/fff"}}
                       {:id 4
                        :name "fuga"
                        :owner {:avatar_url "https://dummyimage.com/64x64/000/fff"}}
                       {:id 5
                        :name "fuga"
                        :owner {:avatar_url "https://dummyimage.com/64x64/000/fff"}}]])

(defn ^:export detail
  [params]
  [component/detail {:id 1
                     :name "hoge"
                     :owner {:avatar_url "https://dummyimage.com/64x64/000/fff"}}])

(loader/set-loaded! :repository)
