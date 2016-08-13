(ns ideamind.data.core-test
  (:require [clojure.test :as t]
            [clojure.spec.test :as st]
            [ideamind.test-util :as tu]
            [ideamind.data.core]))

(defn fixture [f]
  (-> (st/enumerate-namespace 'ideamind.data.core)
      st/instrument)
  (f))

(t/use-fixtures :once fixture)

; Contains tests for namespace ideamind.data
; ---------------------------------------------------------

; utilities for handling trees.

(t/deftest leaf?
  (t/is (tu/check 'ideamind.data.core/leaf?)))

(t/deftest branch?
  (t/is (tu/check 'ideamind.data.core/branch?)))

(t/deftest children
  (t/is (tu/check 'ideamind.data.core/children)))

(t/deftest append
  (t/is (tu/check 'ideamind.data.core/append)))