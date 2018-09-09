(ns sample-github-spa.auth.container
  (:require [cljs.loader :as loader]
            [sample-github-spa.auth.component :as component]))

(defn box
  [params]
  [component/box "/repository"])

(loader/set-loaded! :auth)
