(ns sample-github-spa.auth.effects
  (:require
   [re-frame.core :as re-frame]
   [firebase.app]
   [firebase.auth]))

(re-frame/reg-fx
 ::initialize-firebase
 (fn [config]
   (->> (js/firebase.initializeApp (clj->js config))
        (set! (.-fb js/window)))))

(re-frame/reg-fx
 ::github-login
 (fn []
   (let [provider (doto (js/firebase.auth.GithubAuthProvider.)
                    (.addScope "repo")
                    (.addScope "user"))]
     (.. js/window -fb (auth) (signInWithRedirect provider)))))

(re-frame/reg-fx
 ::get-redirect-result
 (fn [{:keys [on-success on-failure] :as options}]
   (-> (.. js/window -fb auth getRedirectResult)
       (.then #(re-frame/dispatch (conj on-success %)))
       (.catch #(re-frame/dispatch (conj on-failure %))))))
