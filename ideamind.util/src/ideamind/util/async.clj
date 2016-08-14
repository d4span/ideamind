(ns ideamind.util.async
  (:require [clojure.core.async.impl.protocols :as async-protocols]
            [clojure.core.async :as a]
            [clojure.spec.gen :as gen]
            [clojure.spec :as s]))

; Helper methods for dealing with core.async
; ==========================================

(s/def ::channel (s/spec #(satisfies? async-protocols/ReadPort %)
                         :gen (fn [] (gen/return (a/chan)))))