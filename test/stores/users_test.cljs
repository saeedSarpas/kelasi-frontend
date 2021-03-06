(ns stores.users-test
  (:require-macros [mocha-tester.core :refer (describe it before after)]
                   [chaiify.core :refer (expect)]
                   [cljs.core.async.macros :refer (go)])
  (:require [stores.users :as users]
            [actions :as action]
            [backend.session :refer (login)]
            [backend.users   :refer (create)]
            [state :refer (app-state)]
            [cljs.core.async :refer (chan tap untap take!)]
            [mocks.location :as loc]))



(def done-ch (chan))

(def login-data {:username "John" :password "BlahBlah"})

(describe "try-login action"
  (before [done]
    (.stub js/sinon backend.session "login")
    (tap users/done done-ch)
    (action/try-login {:source ::try-login-test
                       :username (:username login-data)
                       :password (:password login-data)})
    (take! done-ch #(done)))

  (after
    (.restore login)
    (untap users/done done-ch))

  (it "should call backend.session/login with username and password"
    (expect (.-calledOnce login) :to-be-true)
    (expect (.calledWithExactly login "John" "BlahBlah") :to-be-true)))



(def user-data {:id "123"})

(describe "load-user action"
  (before [done]
    (tap users/done done-ch)
    (action/load-user {:source ::load-user-test
                       :user   user-data})
    (take! done-ch #(done)))

  (after
    (untap users/done done-ch))

  (it "should put the id of a user under users/all-users"
    (expect (get-in @app-state [:users :all-users (:id user-data)])
            :to-equal user-data)))



(describe "login action"
  (before [done]
    (loc/stub)
    (tap users/done done-ch)
    (action/load-user {:source ::login-test
                       :user   user-data})
    (take! done-ch identity)
    (action/login {:source  ::login-test
                   :user-id (:id user-data)})
    (take! done-ch #(done)))

  (after
    (untap users/done done-ch)
    (loc/unstub))

  (it "should put the user under users/current-user"
    (expect (get-in @app-state [:users :current-user])
            :to-equal user-data)))



(describe "wrong-login action"
  (before [done]
    (swap! app-state assoc-in [:users :current-user] :some-value)
    (tap users/done done-ch)
    (action/wrong-login {:source ::wrong-login-test})
    (take! done-ch #(done)))

  (after
    (untap users/done done-ch))

  (it "should put nil under users/current-user"
    (expect (get-in @app-state [:users :current-user])
            :to-not-exist)))



(describe "signup action"
  (before [done]
    (.stub js/sinon backend.users "create")
    (tap users/done done-ch)
    (action/signup {:source ::signup-test
                    :introducer-id "1"
                    :firstname "John"
                    :lastname "Doe"
                    :university "Taashkand"
                    :email "john@doe.com"
                    :password "123"})
    (take! done-ch #(done)))

  (after
    (.restore create)
    (untap users/done done-ch))

  (it "should call backend.users/create with appropriate parameters"
    (expect (.-calledOnce create) :to-be-true)
    (expect (.calledWithExactly create
                                "John" "Doe" "Taashkand"
                                "john@doe.com" "123" "1")
            :to-be-true)))
