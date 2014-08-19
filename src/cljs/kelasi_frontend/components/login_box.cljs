(ns kelasi-frontend.components.login-box
  (:require [om-tools.core :as omtool :include-macros true]
            [om-tools.dom  :as dom    :include-macros true]
            [kelasi-frontend.actions :refer (try-login)]))



(omtool/defcomponentk login-box
  "First page's login box"
  [[:data {errors {}}] owner state]
  (init-state
   [_]
   {:username    ""
    :password    ""
    :remember-me false})
  (render
   [_]
   (dom/div
    (cond
     (= (get errors :login) :wrong-login)
     (dom/p "Wrong username/password")

     (get errors :network)
     (dom/p "Request failed. retry!"))

    (dom/p "User name"
           (dom/input
            {:type "text"
             :value (:username @state)
             :on-change #(swap! state
                                assoc :username
                                (.. % -target -value))}))
    (dom/p "Password"
           (dom/input
            {:type "password"
             :value (:password @state)
             :on-change #(swap! state
                                assoc :password
                                (.. % -target -value))}))
    (dom/input
     {:type "checkbox"
      :checked (:remember-me @state)
      :on-change #(swap! state update-in [:remember-me] not)})
    (dom/p "Remember me!")
    (dom/button
     {:type "button"
      :on-click #(try-login :source   ::login-box
                            :username (:username @state)
                            :password (:password @state))}
     "Login"))))
