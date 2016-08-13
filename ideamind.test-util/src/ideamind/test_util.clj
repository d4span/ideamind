(ns ideamind.test-util
  (:require [clojure.spec.test :as st]))

(defn check
  ([sym]
   (check sym {::clojure.spec.test.check/opts {:num-tests 40}}))
  ([sym params]
   (let [outcome (st/check sym params)
         data    (-> outcome
                     first)
         failure (:failure data)
         result  (nil? failure)]
     (if (not result)
       (println failure))
     (if (empty? outcome)
       (println "No spec found for" sym))
     (and result (not (empty? outcome))))))
