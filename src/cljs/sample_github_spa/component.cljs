(ns sample-github-spa.component
  (:require [reagent.core :as r]
            [re-frame.core :as re-frame]
            [secretary.core :as secretary]
            [sample-github-spa.events]
            [sample-github-spa.subs]))

(defn- logout
  [{:keys [handle-logout]}]
  [:a {:style {:text-decoration "none"}
       :on-click handle-logout}
   [:span {:style {:font-size "24px"
                   :width "24px"
                   :height "24px"
                   :color "white"}}
    [:i.fas.fa-sign-out-alt]]])

(defn- header
  [{:keys [title handle-logout]}]
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
   (when-not (= title "Login") [logout {:handle-logout handle-logout}])])

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
  (let [router (re-frame/subscribe [::sample-github-spa.subs/router])
        handle-logout #(re-frame/dispatch [::sample-github-spa.events/logout])]
    [:div
     [header {:title (-> @router :title)
              :handle-logout handle-logout}]
     [:div {:style {:padding "60px 0 64px 0"}}
      [(-> @router :component)
       (-> @router :params)]]
     (when-not (= (-> @router :title) "Login") [nav-bar])]))

;; infinite-scroller
;; from https://gist.github.com/nberger/b5e316a43ffc3b7d5e084b228bd83899

(defn- get-scroll-top []
  (if (exists? (.-pageYOffset js/window))
    (.-pageYOffset js/window)
    (.-scrollTop (or (.-documentElement js/document)
                     (.-parentNode (.-body js/document))
                     (.-body js/document)))))

(defn- get-el-top-position [node]
  (if (not node)
    0
    (+ (.-offsetTop node) (get-el-top-position (.-offsetParent node)))))

(defn- safe-component-mounted? [component]
  (try (boolean (r/dom-node component)) (catch js/Object _ false)))

(defn debounce
  "Returns a function that will call f only after threshold has passed without new calls
  to the function. Calls prep-fn on the args in a sync way, which can be used for things like
  calling .persist on the event object to be able to access the event attributes in f"
  ([threshold f] (debounce threshold f (constantly nil)))
  ([threshold f prep-fn]
   (let [t (atom nil)]
     (fn [& args]
       (when @t (js/clearTimeout @t))
       (apply prep-fn args)
       (reset! t (js/setTimeout #(do
                                   (reset! t nil)
                                   (apply f args))
                                threshold))))))

(defn infinite-scroll [props]
  ;; props is a map with :can-show-more? & :load-fn keys
  (let [listener-fn (atom nil)
        detach-scroll-listener (fn []
                                 (when @listener-fn
                                   (.removeEventListener js/window "scroll" @listener-fn)
                                   (.removeEventListener js/window "resize" @listener-fn)
                                   (reset! listener-fn nil)))
        should-load-more? (fn [this]
                            (let [node (r/dom-node this)
                                  scroll-top (get-scroll-top)
                                  my-top (get-el-top-position node)
                                  threshold 50]
                              (< (- (+ my-top (.-offsetHeight node))
                                    scroll-top
                                    (.-innerHeight js/window))
                                 threshold)))
        scroll-listener (fn [this]
                          (when (safe-component-mounted? this)
                            (let [{:keys [load-fn can-show-more?]} (r/props this)]
                              (when (and can-show-more?
                                         (should-load-more? this))
                                ;; component-will-unmountでdetachしていて、ここでdetachする理由が見つからないのでコメントアウトしている
                                ;; 何か意図がありそうだけどここでdetachしたら二回目以降絶対にページング出来ないよね…
                                ;; (detach-scroll-listener)
                                (load-fn)))))
        debounced-scroll-listener (debounce 200 scroll-listener)
        attach-scroll-listener (fn [this]
                                 (let [{:keys [can-show-more?]} (r/props this)]
                                   (when can-show-more?
                                     (when-not @listener-fn
                                       (reset! listener-fn (partial debounced-scroll-listener this))
                                       (.addEventListener js/window "scroll" @listener-fn)
                                       (.addEventListener js/window "resize" @listener-fn)))))]
    (r/create-class
     {:component-did-mount
      (fn [this]
        (attach-scroll-listener this))
      :component-did-update
      (fn [this _]
        (attach-scroll-listener this))
      :component-will-unmount
      detach-scroll-listener
      :reagent-render
      (fn [props]
        [:div])})))
