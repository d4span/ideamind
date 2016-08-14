(ns ideamind.util.java-test
  (:require [ideamind.util.java]
            [clojure.test :refer :all]
            [ideamind.test-util :as tu]))

(deftest java-vararg
  (is (tu/check 'ideamind.util.java/java-vararg
                {::clojure.spec.test.check/opts {:num-tests tu/test-iterations}})))