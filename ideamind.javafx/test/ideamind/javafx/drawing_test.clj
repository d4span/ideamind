(ns ideamind.javafx.drawing-test
  (:require [clojure.test :refer :all]
            [clojure.test.check.clojure-test :as tcct]
            [clojure.test.check.properties :as prop]
            [clojure.test.check.generators :as gen]
            [ideamind.javafx.drawing :as ivd]
            [ideamind.javafx.util.side-effects :as ivus]
            [ideamind.test-util :as it])
  (:import (javafx.scene.paint Color)
           (javafx.scene.layout Background BackgroundFill CornerRadii StackPane BorderPane)
           (java.util List Random)
           (javafx.geometry Insets)
           (javafx.scene Scene)
           (javafx.scene.control Label)
           (javafx.stage Stage)
           (javafx.embed.swing JFXPanel)))

(defn fixture [f]
  (JFXPanel.)
  (f))

(use-fixtures :once fixture)

(def color-gen
  (gen/bind gen/int
            (fn [int] (gen/return (.nextDouble (new Random int))))))

(deftest main-stage
  (testing "Properties of the main stage."
    (let [stackpane               (new StackPane)
          ^Stage stage            (ivus/run-sync (ivd/main-stage stackpane))
          ^Scene scene            (.getScene stage)
          ^BorderPane border-pane (.getRoot scene)]
      (is (= "Ideamind" (.getTitle stage)))
      (is (= 600.0 (.getHeight stage)))
      (is (= 800.0 (.getWidth stage)))
      (is (= stackpane (.getCenter border-pane))))))

(tcct/defspec ->background!
              it/test-iterations
              (prop/for-all [red color-gen
                             green color-gen
                             blue color-gen]
                            (let [color                  (Color/color red green blue)
                                  ^Background background (ivd/->background color)
                                  ^List bgfills          (.getFills background)
                                  ^BackgroundFill bgfill (.get bgfills 0)]
                              (is (= (.getFill bgfill) color))
                              (is (= (.getInsets bgfill) Insets/EMPTY))
                              (is (= (.getRadii bgfill) CornerRadii/EMPTY)))))

(deftest ->Pane!
  (testing "->Pane!"
    (ivd/->Pane)))

;(tcct/defspec ->mapnode
;              it/test-iterations
;              (prop/for-all [tree idc-test/tree-gen]
;                            (let [^StackPane pane (ivd/->mapnode tree)
;                                  ^List children  (.getChildren pane)
;                                  ^Label label    (.get children 1)]
;                              (is (= (.getText label) (str (:data tree)))))))