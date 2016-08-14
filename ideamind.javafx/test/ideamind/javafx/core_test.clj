(ns ideamind.javafx.core-test
  (:require
    [clojure.test :refer :all]
    [ideamind.presentation.core :as ipc]
    [clojure.core.async :as a]
    [ideamind.javafx.core :as ivc]
    [clojure.test.check.clojure-test :as tcct]
    [clojure.test.check.properties :as prop]
    [clojure.test.check.generators :as gen]
    [ideamind.test-util :as it])
  (:import (javafx.scene.layout Pane)
           (com.stuartsierra.component Lifecycle)))

(tcct/defspec view
              it/test-iterations
              (prop/for-all [show-ui gen/boolean]
                            (let [view->pres              (a/chan)
                                  pres->view              (a/chan)
                                  presenter               (ipc/->Presenter view->pres
                                                                           pres->view)
                                  ^Lifecycle view         (assoc (ivc/->View show-ui) :presenter
                                                                                      presenter)
                                  ^Lifecycle started-view (.start view)
                                  ^Pane pane              (:main-pane started-view)]
                              (is (= view->pres (:event-out started-view)))
                              (is (= pres->view (:data-in started-view)))
                              (is (= presenter (:presenter view)))
                              (is (= show-ui (:visible started-view)))
                              (is (not (nil? pane)))
                              (if (nil? (.stop started-view))
                                true
                                true))))



