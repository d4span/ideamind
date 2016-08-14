(defproject ideamind.javafx "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}

  :plugins [[lein-modules "0.3.11"]]

  :profiles {:dev
             {:dependencies [[ideamind/ideamind.test-util :version]]}}

  :dependencies [[org.clojure/clojure "_"]
                 [ideamind/ideamind.presentation :version]]

  :global-vars {*warn-on-reflection* true
                *unchecked-math*     true
                *assert*             true})
