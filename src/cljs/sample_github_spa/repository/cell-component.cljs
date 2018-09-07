(ns sample-github-spa.repository.cell-component)

(defn- image
  [image]
  [:div {:style {:width "84px"
                 :height "84px"
                 :background-image (str "url(" image ")")
                 :background-size "cover"}}])

(defn component
  [repository]
  [:div {:style {:width "40%"
                 :height "168px"
                 :padding "12px 4px 12px 4px"
                 :border-radius "4px"
                 :background-color "white"
                 :display "flex"
                 :align-items "center"
                 :justify-content "space-around"
                 :flex-direction "column"
                 :box-sizing "border-box"}}
   [image (-> repository :owner :avatar_url)]
   [:span {:style {:color "#222831"
                   :margin-left "4px"}} (-> repository :name)]])
