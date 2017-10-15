(ns ideamind.data.core
  (:require
    [clojure.spec.alpha :as s]
    [clojure.spec.gen.alpha :as gen]
    [clojure.test.check.generators :as tcgen]))

; Contains functionality for manipulating data.
; =============================================

(defrecord Tree
  [data left-children right-children])

(s/def ::data (s/spec #(any? %)
                      :gen gen/any-printable))

; Generators for valid trees.
(def data-gen gen/any-printable)

(def leaf-gen
  "Generates an arbitrary leaf."
  (gen/bind (data-gen)
            (fn [data] (gen/return (->Tree data [] [])))))

(def branch-gen
  "Generates a branch."
  (fn [leaf-gen]
    (gen/bind (gen/tuple (gen/vector leaf-gen)
                         (gen/vector leaf-gen)
                         (data-gen))
              (fn [[left-children right-children data]]
                (gen/return (->Tree data left-children right-children))))))

(def tree-gen
  (tcgen/recursive-gen branch-gen leaf-gen))

(s/def ::children (s/spec (s/* ::Tree)))
(s/def ::left-children ::children)
(s/def ::right-children ::children)

; A tree structure used to represent mindmaps.
(s/def ::Tree (s/spec (s/keys :req-un [::data ::left-children ::right-children]
                              :opt-un [])
                      :gen (fn [] tree-gen)))

; Utility methods.
(defn leaf? [tree]
  (and (empty? (:left-children tree))
       (empty? (:right-children tree))))

(s/fdef leaf?
        :args (s/cat :tree ::Tree)
        :ret boolean?
        :fn #(= (:ret %) (and (empty? (:left-children (-> % :args :tree)))
                              (empty? (:right-children (-> % :args :tree))))))

(defn branch? [tree]
  (not (leaf? tree)))

(s/fdef branch?
        :args (s/cat :tree ::Tree)
        :ret boolean?
        :fn #(= (:ret %) (not (leaf? (-> % :args :tree)))))

(defn children [tree]
  (apply conj
         (:left-children tree)
         (:right-children tree)))

(s/fdef children
        :args (s/cat :tree ::Tree)
        :ret (s/* ::Tree)
        :fn #(let [arg (-> % :args :tree)]
              (= (:ret %)
                 (apply conj
                        (:left-children arg)
                        (:right-children arg)))))

(defn append [tree & children]
  (->Tree (:data tree)
          (:left-children tree)
          (apply conj
                 (:right-children tree)
                 children)))

(s/fdef append
        :args (s/cat :tree ::Tree :children (s/* ::Tree))
        :ret ::Tree
        :fn #(and (= (-> % :args :tree :left-children)
                     (-> % :ret :left-children))
                  (= (apply conj
                            (-> % :args :tree :right-children)
                            (-> % :args :children))
                     (-> % :ret :right-children))))