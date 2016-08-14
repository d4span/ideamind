(defproject ideamind/ideamind.presentation "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url  "http://www.eclipse.org/legal/epl-v10.html"}

  :plugins [[lein-modules "0.3.11"]]

  :profiles {:dev
             {:dependencies [[ideamind/ideamind.test-util :version]]}}

  :dependencies [[org.clojure/clojure "_"]
                 [com.stuartsierra/component "_"]
                 [ideamind/ideamind.model :version]
                 [ideamind/ideamind.util :version]
                 [org.clojure/core.async "_"]]

  :global-vars {*warn-on-reflection* true
                *unchecked-math*     true
                *assert*             true})
