(ns sample-github-spa.repository.component
  (:require
   [sample-github-spa.component]))

(defn- grid-item-image
  [image]
  [:div {:style {:width "96px"
                 :height "96px"
                 :background-image (str "url(" image ")")
                 :background-size "cover"}}])

(defn- grid-item
  [repository]
  [:a {:style {:width "40%"
               :height "168px"
               :padding "12px 4px 12px 4px"
               :border-radius "12px"
               :background-color "white"
               :display "flex"
               :align-items "center"
               :justify-content "space-around"
               :flex-direction "column"
               :box-sizing "border-box"
               :text-decoration "none"}
       :href (str "/repository/" (:id repository))}
   [grid-item-image (-> repository :owner :avatar_url)]
   [:span {:style {:color "#222831"
                   :margin-left "4px"
                   :font-weight "600"}}
    (-> repository :name)]])

(defn grid-box
  [{:keys [repositories] :as params}]
  [:div {:style {:display "flex"
                 :justify-content "space-around"
                 :flex-wrap "wrap"
                 :width "100%"}}
   (as-> repositories $
     (map (fn [{:keys [id] :as repository}]
            ^{:key id} [grid-item repository])
          $)
     (if (odd? (count $))
          ;; 要素の数が奇数の時、空のdivを挿入して最後の要素が中央に配置されないようにしている
       (concat $ [^{:key :spacer} [:div {:style {:width "40%"}}]])
       $))])

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
  [repository]
  [:div {:style {:width "100%"
                 :height "100%"
                 :display "flex"
                 :justify-content "space-between"
                 :align-items "center"
                 :flex-direction "column"
                 :padding "32px 0 32px 0"}}
   [detail-image (-> repository :owner :avatar_url)]
   [:div {:style {:width "70%"
                  :box-sizing "border-box"
                  :padding "48px 0 0 0"
                  :font-size "24px"
                  :display "flex"
                  :flex-direction "column"}}
    [detail-information-item "name:" (-> repository :name)]]])
