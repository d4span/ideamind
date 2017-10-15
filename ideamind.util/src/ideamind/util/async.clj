(ns ideamind.util.async
  (:require [clojure.core.async.impl.protocols :as async-protocols]
            [clojure.core.async :as a]
            [clojure.spec.gen.alpha :as gen]
            [clojure.spec.alpha :as s]))

; Helper methods for dealing with core.async
; ==========================================

(s/def ::channel (s/spec #(satisfies? async-protocols/ReadPort %)
                         :gen (fn [] (gen/return (a/chan)))))