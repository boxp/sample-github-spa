(ns sample-github-spa.component
  (:require [reagent.core :as r]
            [re-frame.core :as re-frame]
            [secretary.core :as secretary]
            [sample-github-spa.subs]))

(defn- logout []
  [:a {:style {:text-decoration "none"}
       :href "/"}
   [:span {:style {:font-size "24px"
                   :width "24px"
                   :height "24px"
                   :color "white"}}
    [:i.fas.fa-sign-out-alt]]])

(defn- header
  [title]
  [:div {:style {:width "100%"
                 :height "56px"
                 :display "flex"
                 :justify-content "space-between"
                 :align-items "center"
                 :position "fixed"
                 :padding "0 16px 0 16px"
                 :background-color "#00adb5"
                 :box-sizing "border-box"
                 :box-shadow "2px 2px 2px 2px rgba(0, 0, 0, 0.3)"}}
   [:span {:style {:color "white"
                   :font-size "20px"
                   :font-weight "bold"}}
    title]
   (when-not (= title "Login") [logout])])

(defn- nav-repository []
  [:a {:style {:text-decoration "none"
               :display "flex"
               :justify-content "center"
               :align-items "center"
               :height "100%"
               :width "100%"}
       :href "/repository"}
   [:span {:style {:color "#222831"
                   :font-size "28px"}}
    [:i.fas.fa-book]]])

(defn- nav-activity []
  [:a {:style {:text-decoration "none"
               :display "flex"
               :justify-content "center"
               :align-items "center"
               :height "100%"
               :width "100%"}
       :href "/activity"}
   [:span {:style {:color "#222831"
                   :font-size "28px"}}
    [:i.fas.fa-rss-square]]])

(defn- nav-bar
  []
  [:div {:style {:width "100%"
                 :height "64px"
                 :display "flex"
                 :justify-content "space-around"
                 :align-items "center"
                 :position "fixed"
                 :bottom "0"
                 :box-shadow "2px 2px 2px 2px rgba(0, 0, 0, 0.6)"
                 :background-color "white"}}
   [nav-repository]
   [nav-activity]])

(defn- loading []
  [:span "loading..."])

(defn app []
  (let [router (re-frame/subscribe [::sample-github-spa.subs/router])]
    [:div
     [header (-> @router :title)]
     [:div {:style {:padding "60px 0 64px 0"}}
      [(-> @router :component)
       (-> @router :params)]]
     (when-not (= (-> @router :title) "Login") [nav-bar])]))
