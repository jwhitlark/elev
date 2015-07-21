(ns elev.core
  "Entry point for running simulation scripts."
  (:require [elev.strategy :as s]
            [elev.data.query :as q]
            [elev.data :refer :all]
            [elev.abstract :refer :all])
  (:import [elev.data Elevator ElevatorSim ElevatorCfg])
  (:gen-class))

(defn -main [& args]
  (let [fname (first args)]
    (binding [*ns* (find-ns 'elev.core)]
      (println (format "Running %s" fname))
      ;; I wouldn't normally do this (unsafe), but for a sim it's fine.
      (load-file fname)))
  (System/exit 0))
