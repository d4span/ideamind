(ns ideamind.javafx.util
  (:import (javafx.event EventHandler)))

(defn handler*
  [f]
  (reify EventHandler
    (handle [_ e] (f e))))