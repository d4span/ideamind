(ns ideamind.javafx.core.side-effects
  (:require [clojure.core.async :as a]
            [ideamind.javafx.util.side-effects :as ivus]
            [ideamind.javafx.drawing :as ivd])
  (:import (javafx.stage Stage)
           (javafx.embed.swing JFXPanel)
           (javafx.scene.layout StackPane Pane)
           (javafx.geometry Pos)
           (javafx.scene Scene)))

(defn repaint-mindmap! [ui mindmap]
  (let [^StackPane pane (:main-pane ui)
        label           (ivd/->mapnode mindmap)]
    (doto (.getChildren pane)
      (.add label))
    (doto label
      (StackPane/setAlignment Pos/CENTER))))

(defn start-data-listener! [ui]
  (let [channel (:data-in ui)]
    (a/go-loop [data (a/<! channel)]
      (ivus/run-sync* #(repaint-mindmap! ui data))
      (recur (a/<! channel)))))

(defn show! [ui]
  (do (JFXPanel.)
      (let [^Stage stage (ivus/run-sync (ivd/main-stage (:main-pane ui)))]
        (ivus/run-async (.show stage))
        (start-data-listener! ui))))

(defn close! [ui]
  (let [^Pane pane    (:main-pane ui)
        ^Scene scene  (.getScene pane)
        ^Stage window (.getWindow scene)]
    (.close window)))

