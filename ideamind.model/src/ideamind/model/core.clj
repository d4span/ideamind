(ns ideamind.model.core
  (:require [com.stuartsierra.component :as component]
            [clojure.spec :as s]
            [clojure.spec.gen :as gen]
            [ideamind.data.core :as data])
  (:import (clojure.lang Atom)))

; Contains code related to the model.
; ===================================

(defrecord Model [data]
  component/Lifecycle
  (start [component]
    (assoc component :atom (atom data)))
  (stop [_]))

(s/def ::data ::data/Tree)
(s/def ::atom (s/spec #(and (instance? Atom %)
                            (s/valid? ::data (deref %)))
                      :gen (fn [] (gen/fmap atom (s/gen ::data)))))

(s/def ::Model (s/spec (s/keys :req-un [::data])
                       :gen (fn [] (gen/fmap ->Model (s/gen ::data)))))
(s/def ::Model-started (s/spec (s/and (s/keys :req-un [::data ::atom])
                                      #(= (-> % :atom deref)
                                          (-> % :data)))
                               :gen (fn [] (gen/bind
                                             (s/gen ::Model)
                                             (fn [model] (gen/return (assoc model :atom (atom (:data model)))))))))