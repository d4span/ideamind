(ns ideamind.desktop.core
  (:require [com.stuartsierra.component :as component]
            [ideamind.javafx.core :as view.core]
            [ideamind.presentation.core :as ipc]
            [ideamind.model.core :as imc]
            [ideamind.data.core :as idc]
            [clojure.core.async :as a]
            [clojure.spec :as s]
            [clojure.spec.gen :as gen]))

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

(s/def ::model ::imc/Model)
(s/def ::presenter ::ipc/Presenter)
(s/def ::view ::view.core/View)

(s/def ::show-ui boolean?)
(s/def ::config (s/keys :req-un [::show-ui]))

(s/def ::system (s/keys :req-un [::model ::presenter ::view]
                        :gen (fn [] (gen/bind (gen/tuple (s/gen ::model)
                                                         (s/gen ::presenter)
                                                         (s/gen ::view))
                                              (fn [[model presenter view]]
                                                (gen/return (component/system-map
                                                              :model model
                                                              :presenter presenter
                                                              :view view)))))))

(s/fdef ideamind-system
        :args (s/cat :conf ::config)
        :ret ::system
        :fn #(= (-> % :args :conf :show-ui)
                (-> % :ret :view :visible)))

(defn -main [& _]
  (component/start (ideamind-system {:show-ui true})))
