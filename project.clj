(defn modules
  [output-dir]
  {:repository {:entries #{"sample-github-spa.repository.container"}
                :output-to (str output-dir "/repository.js")
                :depends-on #{:client}}
   :auth {:entries #{"sample-github-spa.auth.container"}
          :output-to (str output-dir "/auth.js")
          :depends-on #{:client}}
   :activity {:entries #{"sample-github-spa.activity.container"}
              :output-to (str output-dir "/activity.js")
              :depends-on #{:client}}
   ;; 分割されたモジュールをロードするために最低限必要なモジュール
   ;; モジュールの分割を行うと必ずこのモジュールが分割されるので出力先ファイル名だけ変更している
   :cljs-base {:output-to (str output-dir "/cljs_base.js")}
   :client {:entries #{"sample-github-spa.client"}
            :output-to (str output-dir "/app.js")
            :depends-on #{:cljs-base}}})

(defproject sample-github-spa "0.1.0-SNAPSHOT"
  :dependencies [[org.clojure/clojure "1.9.0"]
                 [org.clojure/clojurescript "1.10.339"]
                 [reagent "0.10.0"]
                 [re-frame "0.10.6"]
                 [secretary "1.2.3"]
                 [kibu/pushy "0.3.8"]
                 [day8.re-frame/http-fx "0.1.6"]
                 [cljsjs/firebase "5.4.2-1"]]

  :plugins [[lein-cljsbuild "1.1.7"]]

  :min-lein-version "2.5.3"

  :source-paths ["src/clj" "src/cljs"]

  :clean-targets ^{:protect false} ["resources/public/js/compiled" "resources/public/prod/js/compiled" "target"]

  :figwheel {:css-dirs ["resources/public/css"]}

  :profiles
  {:dev
   {:dependencies [[binaryage/devtools "0.9.10"]]
    :plugins      [[lein-figwheel "0.5.16"]
                   [lein-cljfmt "0.6.0"]]}
   :prod { }
   }

  :cljsbuild
  {:builds
   [{:id           "dev"
     :source-paths ["src/cljs" "src/cljs-client"]
     :figwheel     {:on-jsload "sample-github-spa.client/mount-root"}
     :compiler     {:main sample-github-spa.client
                    :output-dir      "resources/public/js/compiled"
                    :asset-path      "/static/js/compiled"
                    :source-map-timestamp true
                    :preloads [devtools.preload]
                    :external-config      {:devtools/config {:features-to-install :all}}
                    :npm-deps false
                    :modules ~(modules "resources/public/js/compiled")}}

    {:id           "prod"
     :source-paths ["src/cljs" "src/cljs-client"]
     :compiler     {:main sample-github-spa.client
                    :output-dir      "resources/public/prod/js/compiled"
                    :asset-path      "/static/js/compiled"
                    :optimizations   :advanced
                    :closure-defines {goog.DEBUG false}
                    :pretty-print    false
                    :npm-deps false
                    :modules ~(modules "resources/public/prod/js/compiled")}}
    {:id           "server-dev"
     :source-paths ["src/cljs" "src/cljs-server"]
     :compiler     {:main sample-github-spa.server
                    :output-dir      "target/server/js/compiled"
                    :output-to      "target/server/js/compiled/server.js"
                    :asset-path      "target/server/js/compiled"
                    :source-map-timestamp true
                    :target :nodejs
                    :npm-deps false
                    :closure-defines {sample-github-spa.server/dev? true
                                      sample-github-spa.server/static-file-path "resources/public/"}
                    :pretty-print    true}}
    {:id           "server-prod"
     :source-paths ["src/cljs" "src/cljs-server"]
     :compiler     {:main sample-github-spa.server
                    :output-dir      "target/server/prod/js/compiled"
                    :output-to      "target/server/prod/js/compiled/server.js"
                    :asset-path      "target/server/prod/js/compiled"
                    :target :nodejs
                    :npm-deps false
                    :closure-defines {sample-github-spa.server/dev? false
                                      sample-github-spa.server/static-file-path "resources/public/prod/"}
                    :pretty-print    false}}]})
