(ns sample-github-spa.repository.list-component
  (:require [sample-github-spa.repository.cell-component :as cell]))

(defn component
  [repositories]
  [:div {:style {:display "flex"
                 :justify-content "space-around"
                 :flex-wrap "wrap"
                 :width "100%"}}
   (map (fn [repository]
          ^{:key (:id repository)} [cell/component repository])
        repositories)])
