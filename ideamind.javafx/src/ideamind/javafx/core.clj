(ns ideamind.javafx.core
  (:require [com.stuartsierra.component :as c]
            [ideamind.presentation.core :as presentation]
            [ideamind.javafx.drawing :as ivd]
            [ideamind.javafx.core.side-effects :as ivcs]
            [ideamind.javafx.util.side-effects :as ivus]
            [clojure.spec :as s]
            [clojure.spec.gen :as gen])
  (:import (javafx.application Platform)
           (javafx.scene.layout Pane)))

(defn- setup-ui [ui mainpane]
  (let [presenter (-> ui :presenter)]
    (-> ui
        (assoc :main-pane mainpane)
        (assoc :event-out (:event-in presenter))
        (assoc :data-in (:data-out presenter)))))

(defrecord View [visible]
  c/Lifecycle

  (start [component]
    (Platform/setImplicitExit false)
    (let [mainpane (ivd/->Pane)
          ui       (setup-ui component mainpane)]
      (if visible (ivcs/show! ui))
      ui))
  (stop [ui]
    (if visible
      (ivus/run-sync (ivcs/close! ui)))
    ui))

;;;;; Specs

(s/def ::pane (s/spec #(instance? Pane %)
                      :gen (fn [] (gen/return (new Pane)))))
(s/def ::main-pane ::pane)
(s/def ::event-out ::presentation/event-in)
(s/def ::data-in ::presentation/data-out)

(s/def ::presenter ::presentation/Presenter)
(s/def ::visible boolean?)

(s/def ::View (s/keys :req-un [::presenter ::visible]
                      :gen (fn [] (gen/bind (gen/tuple (gen/boolean)
                                                       (s/gen ::presenter))
                                            (fn [[v p]] (gen/return (-> (->View v)
                                                                        (assoc :presenter p))))))))

(s/def ::View-started (s/spec (s/and ::View
                                     (s/keys :req-un [::main-pane ::event-out ::data-in])
                                     #(and (= (-> % :event-out)
                                              (-> % :presenter :event-in))
                                           (= (-> % :data-in)
                                              (-> % :presenter :data-out))))
                              :gen (fn [] (gen/bind (gen/tuple (s/gen ::View)
                                                               (s/gen ::main-pane))
                                                    (fn [[view pane]]
                                                      (gen/return (-> view
                                                                      (assoc :main-pane pane)
                                                                      (assoc :event-out (-> view :presenter :event-in))
                                                                      (assoc :data-in (-> view :presenter :data-out)))))))))

(s/fdef setup-ui
        :args (s/cat :view ::View :mainpane ::pane)
        :ret ::View-started
        :fn #(= (-> % :args :mainpane)
                (-> % :ret :main-pane)))