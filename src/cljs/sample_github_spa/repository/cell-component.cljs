(ns sample-github-spa.repository.cell-component)

(defn- image
  [image]
  [:div {:style {:width "56px"
                 :height "56px"
                 :background-image (str "url(" image ")")
                 :background-size "cover"}}])

(defn component
  [repository]
  [:div {:style {:width "100%"
                 :height "64px"
                 :padding "0 4px 0 4px"
                 :border-radius "4px"
                 :background-color "white"
                 :display "flex"
                 :align-items "center"}}
   [:div {:style {:display "flex"
                  :align-items "center"}}
    [image (-> repository :owner :avatar_url)]
    [:span {:style {:color "#222831"
                    :margin-left "4px"}} (-> repository :name)]]])
