(ns sample-github-spa.auth.effects
  (:require
    [re-frame.core :as re-frame]
    [cljsjs.firebase]))

(re-frame/reg-fx
  ::initialize-firebase
  (fn [config]
    (js/firebase.initializeApp (clj->js config))))

(re-frame/reg-fx
  ::github-login
  (fn []
    (let [provider (doto (js/firebase.auth.GithubAuthProvider.)
                     (.addScope "repo")
                     (.addScope "user"))]
      (.. js/firebase auth (signInWithRedirect provider)))))

(re-frame/reg-fx
  ::get-redirect-result
  (fn [{:keys [on-success on-failure] :as options}]
    (-> (.. js/firebase auth getRedirectResult)
        (.then #(re-frame/dispatch (conj on-success %)))
        (.catch #(re-frame/dispatch (conj on-failure %))))))
