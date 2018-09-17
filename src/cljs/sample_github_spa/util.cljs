(ns sample-github-spa.util
  (:require [cljs.loader :as loader]))

(defn universal-load
  [module-name callback-fn]
  (if (= cljs.core/*target* "nodejs")
    (callback-fn)
    (loader/load module-name callback-fn)))

(defn universal-set-loaded!
  [module-name]
  (when-not (= cljs.core/*target* "nodejs")
    (loader/set-loaded! module-name)))
