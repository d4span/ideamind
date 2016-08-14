(ns ideamind.desktop.core
  (:require [com.stuartsierra.component :as component]
            [ideamind.javafx.core :as view.core]
            [ideamind.presentation.core :as ipc]
            [ideamind.model.core :as imc]
            [ideamind.data.core :as idc]
            [clojure.core.async :as a]))

;(def system-config {:show-ui s/Bool})

(defn ideamind-system [config]
  "Initializes the main components in order and supplies
   required dependencies."
  (component/system-map
    :model (imc/->Model (idc/->Tree "Tree" [] []))
    :presenter (component/using
                 (ipc/->Presenter (a/chan) (a/chan))
                 [:model])
    :view (component/using
            (view.core/->View (:show-ui config))
            [:presenter])))

(defn -main [& _]
  (component/start (ideamind-system {:show-ui true})))
