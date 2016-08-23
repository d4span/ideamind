(ns ideamind.javafx.core-test
  (:require
    [clojure.test :refer :all]
    [ideamind.javafx.core :as ivc]
    [clojure.test.check.clojure-test :as tcct]
    [clojure.test.check.properties :as prop]
    [ideamind.test-util :as it]
    [clojure.spec :as s]
    [clojure.spec.test :as st]
    [clojure.test :as t]
    [ideamind.test-util :as tu]))

(defn fixture [f]
  (-> (st/enumerate-namespace 'ideamind.javafx.core)
      st/instrument)
  (f))

(t/use-fixtures :once fixture)

(tcct/defspec view
              it/test-iterations
              (prop/for-all [view (s/gen ::ivc/View)]
                            (s/valid? ::ivc/View-started (.start (-> view (assoc :visible false))))))

(t/deftest setup-ui
  (t/is (tu/check 'ideamind.javafx.core/setup-ui)))



