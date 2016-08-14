(ns ideamind.javafx.util-test
  (:require [ideamind.javafx.util :as ivu]
            [clojure.test.check.clojure-test :as tcct]
            [clojure.test :refer :all]
            [clojure.test.check.properties :as prop]
            [clojure.test.check.generators :as gen]
            [ideamind.test-util :as it])
  (:import (javafx.event EventHandler)))

(def p)

(def data)

(def event-fn-gen
  (gen/bind gen/any
            (fn [any] (gen/return (fn [_] (do
                                            (def data any)
                                            (def p (promise))
                                            (deliver p any)))))))

(tcct/defspec handler*
              it/test-iterations
              (prop/for-all [fun event-fn-gen]
                            (let [^EventHandler handler (ivu/handler* fun)]
                              (is (instance? EventHandler handler))
                              (.handle handler nil)
                              (is (= data @p)))))