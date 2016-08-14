(ns ideamind.javafx.menu-test
  (:require [clojure.test :refer :all]
            [ideamind.javafx.menu :as ivm]
            [clojure.test.check.generators :as gen]
            [clojure.test.check.properties :as prop]
            [clojure.test.check.clojure-test :as tcct]
            [ideamind.test-util :as it])
  (:import (javafx.scene.control MenuItem MenuBar Menu)
           [javafx.embed.swing JFXPanel]
           (java.util Collection)))

;(defn fixture-init [f]
;  (JFXPanel.)
;  (f))
;
;(clojure.test/use-fixtures :once fixture-init)
;
;; Test data generators
;; --------------------
;
;(def handler-gen
;  "Generates a simple EventHandler."
;  (gen/bind
;    gen/any (fn [any] (gen/return
;                        (ideamind.javafx.util/handler*
;                          (fn [_] (println (str any))))))))
;
;(def simple-menuitem-gen
;  "Generates a map representing a simple JavaFX menu item."
;  (gen/bind
;    (gen/tuple (gen/not-empty gen/string-alpha-numeric)
;               handler-gen)
;    (fn [[label handler]] (gen/return {:label   label
;                                       :handler handler}))))
;
;(tcct/defspec test-simple-menuitem-gen
;              it/test-iterations
;              (prop/for-all [menuitem simple-menuitem-gen]
;                            (is (s/validate ivm/menu-schema menuitem))))
;
;(def menu-structure-gen
;  "Generates a JavaFX menu with submenus."
;  (fn [leaf-gen]
;    (gen/bind
;      (gen/tuple (gen/not-empty gen/string-alpha-numeric)
;                 (gen/one-of [(gen/list leaf-gen)
;                              (gen/return nil)]))
;      (fn [[label children]] (gen/return {:label    label
;                                          :children children})))))
;
;(def menu-gen
;  "Generates a structure representing a whole JavaFX menu."
;  (gen/recursive-gen menu-structure-gen simple-menuitem-gen))
;
;(tcct/defspec test-complex-menustructure
;              it/test-iterations
;              (prop/for-all [menu menu-gen]
;                            (is (s/validate ivm/menu-schema menu))))
;
;(def javafx-menu-gen
;  "Generates a sequence of JavaFX menus."
;  (gen/fmap ivm/menutree->menu menu-gen))
;
;; Predicates
;; ----------
;
;(s/defn menutree? [menutree :- ivm/menu-schema]
;  (not-empty (:children menutree)))
;
;(s/defn menutree-children [menutree :- ivm/menu-schema]
;  (:children menutree))
;
;(s/defn menu-branch? [menu]
;  (instance? MenuItem menu))
;
;(s/defn menubar-children [^MenuItem menuitem :- MenuItem]
;  (if (instance? Menu menuitem)
;    (let [^Menu menu (cast Menu menuitem)]
;      (.getItems menu))
;    (empty '())))
;
;(s/defn equivalent? [tuple :- [(s/one ivm/menu-schema "s")
;                               (s/one MenuItem "m")]]
;  (let [menuvec        (first tuple)
;        label          (:label menuvec)
;        handler        (:handler menuvec)
;        ^MenuItem menu (second tuple)]
;    (and (= label (.getText menu))
;         (if (not (vector? handler))
;           (identical? handler (.getOnAction menu))
;           (instance? Menu menu)))))
;
;; Specs
;
;(defn menu-valid? [^MenuItem menuitem label handler]
;  (and (= label (. menuitem getText))
;       (identical? handler (. menuitem getOnAction))))
;
;(tcct/defspec ->menuitem
;              it/test-iterations
;              (prop/for-all [label gen/string
;                             handler handler-gen]
;                            (let [^MenuItem menuitem (ivm/->menuitem label handler)]
;                              (is (menu-valid? menuitem label handler)))))
;
;(tcct/defspec map->menuitem
;              it/test-iterations
;              (prop/for-all [item-map simple-menuitem-gen]
;                            (let [^MenuItem menuitem (ivm/map->menuitem item-map)
;                                  label              (:label item-map)
;                                  handler            (:handler item-map)]
;                              (is (menu-valid? menuitem label handler)))))
;
;(tcct/defspec ->menu
;              it/test-iterations
;              (prop/for-all [item-maps (gen/list simple-menuitem-gen)
;                             menu-name gen/string]
;                            (let [menuitems         (map ivm/map->menuitem item-maps)
;                                  ^Menu menu        (apply (partial ivm/->menu menu-name)
;                                                           menuitems)
;                                  ^Collection items (.getItems menu)]
;                              (is (= menu-name (.getText menu)))
;                              (is (every? true? (map #(.contains items %)
;                                                     menuitems))))))
;
;(tcct/defspec menutree->menu
;              it/test-iterations
;              (prop/for-all [menutree menu-gen]
;                            (let [^MenuBar menubar (ivm/menutree->menu menutree)
;                                  menumapseq       (tree-seq menutree? menutree-children menutree)
;                                  menubarseq       (tree-seq menu-branch? menubar-children menubar)
;                                  pairs            (map vector menumapseq menubarseq)
;                                  comps            (map equivalent? pairs)]
;                              (is (= (count menumapseq) (count menubarseq)))
;                              (is (not-any? false? comps)))))
;
;(tcct/defspec ->menubar
;              it/test-iterations
;              (prop/for-all [menus (gen/not-empty (gen/list javafx-menu-gen))]
;                            (let [^MenuBar menubar (apply ivm/->menubar menus)
;                                  menus-from-bar   (.getMenus menubar)
;                                  menucomparison   (map identical? menus-from-bar menus)]
;                              (is (= (count menus) (count menus-from-bar) (count menucomparison)))
;                              (is (every? true? menucomparison)))))