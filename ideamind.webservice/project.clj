(defproject ideamind/ideamind.webservice "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :min-lein-version "2.0.0"
  :plugins [[lein-modules "0.3.11"]
            [lein-ring "0.9.7"]]


  :dependencies [[org.clojure/clojure "_"]
                 [compojure "_"]
                 [ring/ring-defaults "_"]]

  :ring {:handler ideamind.webservice.handler/app}

  :profiles {:dev {:dependencies [[javax.servlet/servlet-api "_"]
                                  [ring/ring-mock "_"]]}})
