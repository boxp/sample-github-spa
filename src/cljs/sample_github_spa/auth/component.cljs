(ns sample-github-spa.auth.component)

(defn- github-login-button
  [handle-click-github-login-button]
  [:a {:style {:height "60px"
               :background-color "#393e46"
               :display "flex"
               :justify-content "center"
               :align-items "center"
               :padding "0 24px 0 24px"
               :border-radius "8px"
               :text-decoration "none"
               :color "#eeeeee"}
       :on-click handle-click-github-login-button}
   [:span {:style {:width "28px"
                   :height "28px"
                   :font-size "28px"}}
    [:i.fab.fa-github]]
   [:span {:style {:font-weight "bold"
                   :margin-left "12px"}}
    "Login with GitHub"]])

(defn box
  [handle-click-github-login-button]
  [:div {:style {:width "100%"
                 :height "30vh"
                 :margin-top "20vh"
                 :display "flex"
                 :flex-direction "column"
                 :justify-content "space-around"
                 :align-items "center"}}
   [:div
    [:span {:style {:color "#393e46"
                    :font-weight "bold"
                    :font-size "32px"}}
     "GitHub SPA Sample"]]
   [github-login-button handle-click-github-login-button]])
