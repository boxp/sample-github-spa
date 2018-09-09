(ns sample-github-spa.activity.component)

(defn- timeline-item
  [activity]
  [:a {:style {:width "100%"
               :height "64px"
               :display "flex"
               :justify-content "flex-start"
               :align-items "center"
               :text-decoration "none"}
       :href (str "/activity/" (:id activity))}
   [:div {:style {:width "64px"
                  :height "64px"
                  :background-image (str "url(" (-> activity :actor :avatar_url) ")")
                  :background-size "cover"}}]
   [:span {:style {:font-size "24px"
                   :margin-left "16px"
                   :color "#393e46"}}
    (-> activity :payload)]])

(defn timeline
  [activities]
  [:div {:style {:width "100%"
                 :display "flex"
                 :justify-content "flex-start"
                 :flex-direction "column"
                 :box-sizing "border-box"
                 :padding "0 16px 12px 16px"}}
   (map (fn [activity]
          ^{:key (:id activities)} [:div {:style {:margin-top "12px"}} [timeline-item activity]])
        activities)])

(defn- detail-image
  [image]
  [:div {:style {:width "128px"
                 :height "128px"
                 :background-image (str "url(" image ")")
                 :background-size "cover"}}])

(defn- detail-information-item
  [title content]
  [:div {:style {:display "flex"
                 :justify-content "space-between"
                 :color "#222831"
                 :width "100%"}}
   [:span title]
   [:span content]])

(defn detail
  [activity]
  [:div {:style {:width "100%"
                 :height "100%"
                 :display "flex"
                 :justify-content "space-between"
                 :align-items "center"
                 :flex-direction "column"
                 :padding "32px 0 32px 0"}}
   [detail-image (-> activity :actor :avatar_url)]
   [:div {:style {:width "70%"
                  :box-sizing "border-box"
                  :padding "48px 0 0 0"
                  :font-size "24px"
                  :display "flex"
                  :flex-direction "column"}}
    [detail-information-item "payload:" (-> activity :payload)]]])
