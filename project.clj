(defproject ideamind "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url  "http://www.eclipse.org/legal/epl-v10.html"}

  :plugins [[lein-modules "0.3.11"]]

  :profiles {:dev {:dependencies [[org.clojure/test.check "_"]]}}

  :modules {:versions {org.clojure/clojure                          "1.10.3"
                       org.clojure/test.check                       "1.1.1"
                       com.stuartsierra/component                   "1.1.0"
                       org.clojure/core.async                       "1.5.648"
                       org.openjfx/javafx-controls                  "17.0.2"
                       org.openjfx/javafx-swing                     "17.0.2"}})