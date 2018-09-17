(ns sample-github-spa.auth.events
  (:require
   [re-frame.core :as re-frame]
   [sample-github-spa.effects]
   [sample-github-spa.coeffects]
   [sample-github-spa.auth.effects :as effects]))

(re-frame/reg-event-fx
 ::initialize
 (fn [{:keys [db]} _]
   {::effects/initialize-firebase {:apiKey "AIzaSyAHnXLVFFHSPnytiB1t_4ClP-zZ4QUkYlI",
                                   :authDomain "sample-github-spa.firebaseapp.com",
                                   :databaseURL "https://sample-github-spa.firebaseio.com",
                                   :projectId "sample-github-spa",
                                   :storageBucket "sample-github-spa.appspot.com",
                                   :messagingSenderId "227585262670"}
    :dispatch-n [[::get-access-token]
                 [::login-with-access-token]]}))

(re-frame/reg-event-fx
 ::login
 (fn [{:keys [db]} _]
   {::effects/github-login {}}))

(re-frame/reg-event-fx
 ::store-token
 (fn [{:keys [db]} [_ access-token]]
   {:db (-> db
            (assoc :token access-token))
    ::sample-github-spa.effects/set-localstorage ["access-token" access-token]}))

(re-frame/reg-event-fx
 ::redirect-to-home
 (fn [_ _]
   {::sample-github-spa.effects/route ["/repository"]}))

(re-frame/reg-event-fx
 ::on-success-get-access-token
 (fn [{:keys [db]} [_ result]]
   (let [access-token (some-> result .-credential .-accessToken)]
     (when access-token
       {:dispatch-n [[::store-token access-token]
                     [::redirect-to-home]]}))))

(re-frame/reg-event-db
 ::on-failure-get-access-token
 (fn [db [_ error]]
   (-> db
       (assoc :api-error error))))

(re-frame/reg-event-fx
 ::get-access-token
 (fn [{:keys [db]} _]
   {::effects/get-redirect-result {:on-success [::on-success-get-access-token]
                                   :on-failure [:on-failure-get-access-token]}}))

(re-frame/reg-event-fx
 ::login-with-access-token
 [(re-frame/inject-cofx ::sample-github-spa.coeffects/local-store "access-token")]
 (fn [{:keys [db local-store]} _]
   (when local-store
     {:db (assoc db :token local-store)
      ::sample-github-spa.effects/route ["/repository"]})))
