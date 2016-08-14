(ns ideamind.desktop.core-test
  (:require [clojure.test :refer :all]
            [ideamind.desktop.core :as core]
            [com.stuartsierra.component :as component]
            [clojure.test.check.clojure-test :as ctcc]
            [clojure.test.check.properties :as prop]
            [clojure.test.check.generators :as gen]
            [ideamind.test-util :as it])
  (:import [ideamind.model.core Model]
           [ideamind.presentation.core Presenter]
           [ideamind.javafx.core View]))

(ctcc/defspec system-initialization
              it/test-iterations
              (prop/for-all [show-ui gen/boolean]
                            (let [system    (core/ideamind-system {:show-ui show-ui})
                                  model     (:model system)
                                  presenter (:presenter system)
                                  ui        (:view system)]
                              (is (instance? Model model))
                              (is (instance? Presenter presenter))
                              (is (instance? View ui)))))

(ctcc/defspec system-startup
              it/test-iterations
              (prop/for-all [show-ui gen/boolean]
                            (let [system    (core/ideamind-system {:show-ui show-ui})
                                  started   (component/start system)
                                  model     (:model started)
                                  presenter (:presenter started)
                                  view      (:view started)]
                              (is (= model (:model presenter)))
                              (is (= presenter (:presenter view)))
                              (component/stop started))))
