(ns ideamind.javafx.drawing
  (:require [ideamind.javafx.menu :as ivm]
            [ideamind.util.java :as iuj]
            [ideamind.data.core :as data])
  (:import (javafx.scene.layout Background BackgroundFill CornerRadii StackPane BorderPane Pane)
           (javafx.scene.control Label)
           (javafx.geometry Insets)
           (javafx.scene.paint Color)
           (javafx.scene.shape Rectangle)
           (javafx.scene Scene)
           (javafx.stage Stage)
           (ideamind.data.core Tree)))

; Code related to drawing mindmaps and the user interface.
; ========================================================

(defn main-stage
  "Creates the main container for the user interface."
  [mainpane]
  (let [stage       (new Stage)
        border-pane (new BorderPane)
        scene       (new Scene border-pane)
        menu        (ivm/menutree->menu ivm/menu-structure)
        menubar     (ivm/->menubar menu)]
    (doto border-pane
      (.setTop menubar)
      (.setCenter mainpane))
    (doto stage
      (.setTitle "Ideamind")
      (.setHeight 600)
      (.setWidth 800)
      (.setScene scene))
    stage))

(defn ->background [color]
  (new Background (iuj/java-vararg BackgroundFill (new BackgroundFill
                                                       color
                                                       CornerRadii/EMPTY
                                                       Insets/EMPTY))))

(defn ->Pane []
  "Creates a new JavaFX Pane used for drawing the mindmap."
  (let [pane (new StackPane)]
    pane))

(defn ->mapnode [tree]
  (let [stackpane   (new StackPane)
        ^Label text (new Label (str (:data tree)))
        ellipse     (new Rectangle)]
    (doto ellipse
      (.setWidth 60)
      (.setHeight 40)
      (.setOpacity 0.25))
    (doto (.getChildren stackpane)
      (.add ellipse)
      (.add text))
    stackpane))