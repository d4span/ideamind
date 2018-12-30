(ns ideamind.javafx.core
  (:require [com.stuartsierra.component :as c]
            [ideamind.presentation.core :as presentation]
            [ideamind.javafx.drawing :as ivd]
            [ideamind.javafx.core.side-effects :as ivcs]
            [ideamind.javafx.util.side-effects :as ivus]
            [clojure.spec.alpha :as s]
            [clojure.spec.gen.alpha :as gen])
  (:import (javafx.application Platform)
           (javafx.scene.layout Pane)))

(defn- setup-ui [ui presenter mainpane]
  (-> ui
      (assoc :main-pane mainpane)
      (assoc :presenter presenter)
      (assoc :event-out (:event-in presenter))
      (assoc :data-in (:data-out presenter))))

(defrecord View [visible]
  c/Lifecycle

  (start [component]
    (Platform/setImplicitExit false)
    (let [mainpane (ivd/->Pane)
          ui (setup-ui component (:presenter component) mainpane)]
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

(s/def ::View (s/keys :req-un [::visible]
                      :gen (fn [] (gen/bind (gen/boolean)
                                            (fn [visible] (gen/return (-> (->View visible))))))))

(s/def ::View-started (s/spec (s/and ::View
                                     (s/keys :req-un [::presenter ::main-pane ::event-out ::data-in])
                                     #(and (= (-> % :event-out)
                                              (-> % :presenter :event-in))
                                           (= (-> % :data-in)
                                              (-> % :presenter :data-out))))
                              :gen (fn [] (gen/bind (gen/tuple (s/gen ::View)
                                                               (s/gen ::presenter)
                                                               (s/gen ::main-pane))
                                                    (fn [[view presenter pane]]
                                                      (gen/return (-> view
                                                                      (assoc :main-pane pane)
                                                                      (assoc :presenter presenter)
                                                                      (assoc :event-out (-> view :presenter :event-in))
                                                                      (assoc :data-in (-> view :presenter :data-out)))))))))

(s/fdef setup-ui
        :args (s/cat :view ::View :pres ::presenter :mainpane ::pane)
        :ret ::View-started
        :fn #(and (= (-> % :args :mainpane)
                     (-> % :ret :main-pane))
                  (= (-> % :args :pres)
                     (-> % :ret :presenter))
                  (= (-> % :args :pres :event-in)
                     (-> % :ret :event-out))
                  (= (-> % :args :pres :data-out)
                     (-> % :ret :data-in))))