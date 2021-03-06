(ns components.find-friends-box
  (:require [reagent.core :as r]
            [widgets.input :refer (input)]
            [widgets.button :refer (button)]))



(defn find-friends-box
  "First step of registration."
  [on-search]
  (let [firstname  (r/atom "")
        lastname   (r/atom "")
        university (r/atom "")]
    (fn [_]
      [:div
       [:p "Enter your friend's data:"]

       [:div "First name:"
        [input :text firstname]]

       [:div "Last name:"
        [input :text lastname]]

       [:div "University name:"
        [input :text university]]

       [button "Search" #(on-search @firstname @lastname @university)]])))
