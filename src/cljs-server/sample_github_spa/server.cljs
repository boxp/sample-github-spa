(ns sample-github-spa.server
  (:require
    [cljs.reader :as reader]
    [cljs.loader :as loader]
    [re-frame.core :as re-frame]
    [re-frame.db :as db]
    [reagent.dom.server :as r]
    [secretary.core :as secretary]
    [sample-github-spa.repository.container]
    [sample-github-spa.activity.container]
    [sample-github-spa.auth.container]
    [sample-github-spa.component :as component]
    [sample-github-spa.config :as config]
    [sample-github-spa.events :as events]
    [sample-github-spa.route]))

(def express (js/require "express"))
(def ^:export app (express))

(goog-define static-file-path "/")
(goog-define dev? false)

(defn dev-setup []
  (when config/debug?
    (enable-console-print!)
    (println "dev mode")))

(defn index []
  [:html {:lang "en"}
   [:head
    [:meta {:charset "utf-8"}]
    [:meta {:name "viewport"
            :content "width=device-width,initial-scale=1"}]
    [:meta {:name "theme-color" :content "#00adb5"}]
    (when-not dev?
      [:link {:rel "manifest" :href "/manifest.json"}])
    [:style "
      body {
        font-family: -apple-system, BlinkMacSystemFont, "Helvetica Neue", YuGothic, "ヒラギノ角ゴ ProN W3", Hiragino Kaku Gothic ProN, Arial, "メイリオ", Meiryo, sans-serif;
      }

      /* http://meyerweb.com/eric/tools/css/reset/
         v2.0 | 20110126
       License: none (public domain)
       */

      html, body, div, span, applet, object, iframe,
      h1, h2, h3, h4, h5, h6, p, blockquote, pre,
      a, abbr, acronym, address, big, cite, code,
      del, dfn, em, img, ins, kbd, q, s, samp,
      small, strike, strong, sub, sup, tt, var,
      b, u, i, center,
      dl, dt, dd, ol, ul, li,
      fieldset, form, label, legend,
      table, caption, tbody, tfoot, thead, tr, th, td,
      article, aside, canvas, details, embed,
      figure, figcaption, footer, header, hgroup,
      menu, nav, output, ruby, section, summary,
      time, mark, audio, video {
        margin: 0;
        padding: 0;
        border: 0;
        font-size: 100%;
        font: inherit;
        vertical-align: baseline;
      }
      /* HTML5 display-role reset for older browsers */
      article, aside, details, figcaption, figure,
      footer, header, hgroup, menu, nav, section {
        display: block;
      }
      body {
        line-height: 1;
      }
      ol, ul {
        list-style: none;
      }
      blockquote, q {
        quotes: none;
      }
      blockquote:before, blockquote:after,
      q:before, q:after {
        content: '';
        content: none;
      }
      table {
        border-collapse: collapse;
        border-spacing: 0;
      }
     "]]
   [:body
    [:div#app
     [component/app]]
    ;; 本当はcljsjs/firebaseを利用すべきだが、SSR時のfirebase-app.inc.js:1:200からの 'TypeError: Cannot read property 'fetch' of undefined' エラーを解消出来ず諦めてglobal変数に展開した
    ;; コードを見ると !(typeof self != "undefined"?self:void 0).fetch ってどうあがいてもundefined.fetchになりませんか…
    [:script {:src "https://www.gstatic.com/firebasejs/5.5.0/firebase-app.js"}]
    [:script {:src "https://www.gstatic.com/firebasejs/5.5.0/firebase-auth.js"}]
    [:script {:src "/static/js/compiled/cljs_base.js"}]
    [:script {:src "/static/js/compiled/app.js"}]]
   [:link {:rel "stylesheet"
           :href "https://use.fontawesome.com/releases/v5.3.1/css/all.css"
           :integrity "sha384-mzrmE5qonljUremFsqc01SB46JvROS7bZs3IO2EmfFsd15uHvIt+Y8vEf7N7fWAU"
           :crossOrigin "anonymous"}]
   [:div
    {:dangerouslySetInnerHTML
     {:__html  (str "<script>window.preload = '" (-> @db/app-db pr-str) "'</script>")}}]
   (when-not dev?
     [:div
      {:dangerouslySetInnerHTML
       {:__html "
        <script>
        // Check that service workers are registered
        if ('serviceWorker' in navigator) {
            // Use the window load event to keep the page load performant
            window.addEventListener('load', () => {
                  navigator.serviceWorker.register('/sw.js');
            });
        }
        </script>"}}])])

(defn handle-render
  [req res]
  (let [request-path (.-baseUrl req)]
    (re-frame/dispatch-sync [::events/initialize])
    (secretary/dispatch! request-path)
    (.format res #js {"text/html" #(.send res (r/render-to-string [index]))})))

(defn serve
  [path]
  (.listen app path))

(defn -main
  [& args]
  (let [path (-> args first js/parseInt)]
    (dev-setup)
    (serve path)))

(doto app
  (.use "/sw.js" (.static express (str static-file-path "sw.js")))
  (.use "/manifest.json" (.static express (str static-file-path "manifest.json")))
  (.use "static" (.static express static-file-path))
  (.use "/static" (.static express static-file-path))
  (.use "/*" handle-render))

(set! *main-cli-fn* -main)
