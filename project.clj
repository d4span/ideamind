(defproject ideamind "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url  "http://www.eclipse.org/legal/epl-v10.html"}

  :plugins [[lein-modules "0.3.11"]]

  :profiles {:dev {:dependencies [[com.gfredericks.forks.org.clojure/test.check "_"]]}}

  :modules {:versions {org.clojure/clojure                          "1.10.0"
                       com.gfredericks.forks.org.clojure/test.check "0.10.0-PREVIEW-1"
                       com.stuartsierra/component                   "0.3.1"
                       org.clojure/core.async                       "0.4.490"
                       org.openjfx/javafx-controls                  "11.0.1"
                       org.openjfx/javafx-swing                     "11.0.1"}})