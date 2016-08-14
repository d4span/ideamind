(ns ideamind.javafx.util.side-effects
  (:import (javafx.application Platform)))

(defn run-async*
  "Asynchonouosly executes a function on the JavaFX UI thread."
  [f]
  (Platform/runLater f))

(defmacro run-async
  "Asynchonouosly executes code on the JavaFX UI thread."
  [& code]
  `(run-async* (fn [] ~@code)))

(defn run-sync*
  "Synchonouosly executes a function on the JavaFX UI thread."
  [f]
  (let [result (promise)]
    (run-async
      (deliver result (try (f) (catch Throwable e
                                 (println e)
                                 e))))
    @result))

(defmacro run-sync
  "Synchonouosly executes code on the JavaFX UI thread."
  [& body]
  `(run-sync* (fn [] ~@body)))