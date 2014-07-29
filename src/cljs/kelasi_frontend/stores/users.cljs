(ns kelasi-frontend.stores.users
  (:require-macros [cljs.core.async.macros :refer (go)])
  (:require [kelasi-frontend.state :refer (app-state)]
            [kelasi-frontend.dispatcher :refer (create-chan)]
            [kelasi-frontend.backend.session :as session]
            [cljs.core.async :refer (<!)]))



;; All actions come through this channel

(def actions-chan (create-chan))



;; State manipulation functions

(defn- set-in
  "Set the value in the subpath of app-state"
  [subpath value]
  (assert (vector? subpath))
  (swap! app-state
         assoc-in (cons :users (seq subpath))
         value))



;; Action response functions

(defn- do-try-login
  "try_login action has been received."
  [{:keys [user-name password]}]
  (set-in [:current_user] :loading)
  (session/login user-name password))



;; Listen for actions (main loop)

(go (while true
      (let [action (<! actions-chan)]
        (condp = (:action action)
          :try-login (do-try-login (:payload action))
          nil))))
