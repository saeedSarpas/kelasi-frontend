(ns kelasi-frontend.actions
  (:require [kelasi-frontend.dispatcher :as dispatch]
            [schema.core :as s]))



(def try-login-schema
  {:source   s/Keyword
   :username s/Str
   :password s/Str})

(defn try-login
  "View - When user submits login box"
  [& {:as params}]
  (s/validate try-login-schema params)
  (dispatch/dispatch (merge params {:action :try-login})))



(def load-user-schema
  {:source s/Keyword
   :user   {s/Any s/Any}})

(defn load-user
  "Backend - When the server send user"
  [& {:as params}]
  (s/validate load-user-schema params)
  (dispatch/dispatch (merge params {:action :load-user})))



(def login-schema
  {:source  s/Keyword
   :user-id s/Str})

(defn login
  "Backend - When user logs in with the backend"
  [& {:as params}]
  (s/validate login-schema params)
  (dispatch/dispatch (merge params {:action :login})))



(def wrong-login-schema
  {:source s/Keyword})

(defn wrong-login
  "Backend - When username/password is wrong"
  [& {:as params}]
  (s/validate wrong-login-schema params)
  (dispatch/dispatch (merge params {:action :wrong-login})))



(def net-error-schema
  {:source s/Keyword
   :orig   s/Any})

(defn net-error
  "Backend - Whenever something goes wrong with a request"
  [& {:as params}]
  (s/validate net-error-schema params)
  (dispatch/dispatch (merge params {:action :net-error})))
