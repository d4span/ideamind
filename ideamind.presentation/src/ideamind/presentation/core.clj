(ns ideamind.presentation.core
  (:require [clojure.core.async :as a]
            [com.stuartsierra.component :as c]
            [clojure.spec.alpha :as s]
            [clojure.spec.gen.alpha :as gen]
            [ideamind.model.core :as model]
            [ideamind.util.async :as async]))

; Contains code related to the Presenter.
; =======================================

(defrecord Presenter [event-in data-out]
  c/Lifecycle
  (start [p]
    ; Put the data currently in the model in the output channel.
    (a/go (a/>! data-out (deref (:atom (:model p)))))
    p)
  (stop [_]))

(s/def ::event-in ::async/channel)
(s/def ::data-out ::async/channel)
(s/def ::model ::model/Model-started)

(s/def ::Presenter (s/spec (s/keys :req-un [::event-in ::data-out])
                           :gen (fn [] (gen/bind (gen/tuple (s/gen ::event-in)
                                                            (s/gen ::data-out))
                                                 (fn [[event-in data-out]]
                                                   (gen/return (-> (->Presenter event-in data-out))))))))

(s/def ::Presenter-started (s/spec (s/and ::Presenter
                                          (s/keys :req-un [::model]))
                                   :gen (fn [] (gen/bind (gen/tuple (s/gen ::model/Model-started)
                                                                    (s/gen ::Presenter))
                                                         (fn [[model presenter]]
                                                           (gen/return (assoc presenter :model model)))))))