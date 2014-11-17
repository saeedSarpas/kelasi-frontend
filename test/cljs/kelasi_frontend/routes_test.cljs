(ns kelasi-frontend.routes-test
  (:require-macros [mocha-tester.core :refer (describe it)]
                   [chaiify.core :refer (expect)])
  (:require [kelasi-frontend.routes :as routes]))



(describe "home-path"
  (it "should point to '/'"
    (expect (routes/home-path) :to-equal "/")))



(describe "sub-path"
  (it "should be available"
    (expect (routes/sub-path {:path "test"}) :to-equal "/test")))
