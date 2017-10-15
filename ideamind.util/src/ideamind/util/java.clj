(ns ideamind.util.java
  (:require [clojure.spec.alpha :as s]
            [clojure.spec.gen.alpha :as gen]))

; Helper methods for dealing with Java interop
; ============================================

(defn java-vararg [^Class class & args]
  (into-array class args))

(s/def ::java-vararg-args (s/spec (s/and (s/cat :Class #(instance? Class %)
                                                :args (s/+ any?))
                                         #(every? (fn [obj] (instance? (-> % :Class) obj))
                                                  (-> % :args)))
                                  :gen (fn [] (gen/bind (gen/any)
                                                        (fn [arg] (gen/return (apply list (vector (class arg) arg))))))))
(s/def ::java-array #(.isArray (class %)))

(s/fdef java-vararg
        :args ::java-vararg-args
        :ret ::java-array
        :fn #(every? (fn [obj] (instance? (-> % :args :Class) obj))
                     (-> % :ret)))