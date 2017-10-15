(ns ideamind.util.java-test
  (:require [ideamind.util.java]
            [clojure.test :refer :all]
            [ideamind.test-util :as tu]
            [clojure.test :as t]))

(defn fixture [f]
  (tu/instrument-namespaces)
  (f))

(t/use-fixtures :once fixture)

(deftest java-vararg
  (is (tu/check 'ideamind.util.java/java-vararg
                {:clojure.spec.test.check/opts {:num-tests tu/test-iterations}})))