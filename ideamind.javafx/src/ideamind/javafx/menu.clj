(ns ideamind.javafx.menu
  (:import (javafx.scene.control MenuItem MenuBar Menu)
           (javafx.event EventHandler))
  (:require [ideamind.javafx.util :as util]))

; Schema for describing menus
; ---------------------------

;(def has-handler? (fn [n] (not (nil? (:handler n)))))
;
;(def menuitem-schema {:label s/Str :handler EventHandler})
;
;(def menu-schema (s/if has-handler?
;                   menuitem-schema
;                   {:label s/Str :children [(s/recursive #'menu-schema)]}))

; Defines the actual menu structure
; ---------------------------------

(def menu-structure
  {:label "File" :children [{:label   "Close"
                             :handler (util/handler*
                                        (fn [_] (println "Close!")))}]})

; A small DSL for defining JavaFX menus.

(defn ->menuitem
  "Helper method for creating menu items."
  [caption handler]
  (let [menuitem (new MenuItem caption)]
    (doto menuitem
      (.setOnAction handler))
    menuitem))

(defn map->menuitem [menuitem]
  (->menuitem (:label menuitem) (:handler menuitem)))

(defn ->menu
  "Helper method for creating a complete menu."
  [title & items]
  (let [menu (new Menu title)
        icol (.getItems menu)]
    (doseq [item items]
      (.add icol item))
    menu))

(defn menutree->menu
  "Creates a JavaFX Menu from a menu tree."
  [menu]
  (let [^EventHandler handler (:handler menu)
        label                 (:label menu)
        children              (:children menu)]
    (if handler
      (->menuitem label handler)
      (apply (partial ->menu label) (for [submenu children]
                                      (menutree->menu submenu))))))

(defn ->menubar
  "Creates a MenuBar containing the supplied Menus."
  [& menus]
  (let [menubar (new MenuBar)
        mcol    (.getMenus menubar)]
    (doseq [menu menus]
      (.add mcol menu))
    menubar))
