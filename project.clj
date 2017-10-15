(defproject ideamind "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url  "http://www.eclipse.org/legal/epl-v10.html"}

  :plugins [[lein-modules "0.3.11"]]

  :profiles {:dev {:dependencies [[com.gfredericks.forks.org.clojure/test.check "_"]]}}

  :modules {:versions {org.clojure/clojure                          "1.9.0-beta2"
                       org.clojure/clojurescript                    "1.9.229"
                       com.gfredericks.forks.org.clojure/test.check "0.10.0-PREVIEW-1"
                       com.stuartsierra/component                   "0.3.1"
                       org.clojure/core.async                       "0.2.391"
                       compojure                                    "1.5.1"
                       ring/ring-defaults                           "0.2.1"
                       javax.servlet/servlet-api                    "2.5"
                       ring/ring-mock                               "0.3.0"
                       binaryage/devtools                           "0.8.2"
                       figwheel-sidecar                             "0.5.8"
                       com.cemerick/piggieback                      "0.2.1"}})