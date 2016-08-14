(ns ideamind.javafx.core
  (:require [com.stuartsierra.component :as c]
            [ideamind.javafx.drawing :as ivd]
            [ideamind.javafx.core.side-effects :as ivcs]
            [ideamind.javafx.util.side-effects :as ivus])
  (:import (javafx.application Platform)))

(defn- setup-ui [ui mainpane presenter]
  (-> ui
      (assoc :main-pane mainpane)
      (assoc :event-out (:event-in presenter))
      (assoc :data-in (:data-out presenter))))

(defrecord View [visible]
  c/Lifecycle

  (start [component]
    (Platform/setImplicitExit false)
    (let [mainpane  (ivd/->Pane)
          presenter (:presenter component)
          ui        (setup-ui component mainpane presenter)]
      (if visible (ivcs/show! ui))
      ui))
  (stop [ui]
    (if visible
      (ivus/run-sync (ivcs/close! ui)))
    ui))