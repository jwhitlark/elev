(ns user
  "Experimental namespace, autoloaded in any REPL."
  (:require [clojure.pprint :as pp]
            [elev.strategy :as s]
            [elev.data.query :as q]
            [elev.data :refer :all]
            [elev.abstract :refer :all]
            )
  (:import [elev.data Elevator ElevatorSim ElevatorCfg])
  )


#_ (-> (make-elevator-sim (->ElevatorCfg s/closest-stopped-or-random-strategy 5 16))
       (pickup 2 4)
       (tick-view)
       (tick-view)
       (pickup 3 0)
       (tick-view)
       (tick-view)
       (pickup 4 1)
       ;; (tick-view-until-finished)
       ;; (tick-view)
       ;; (tick-view)
       ;; (tick-view)
       )


#_ (-> (make-elevator-sim (->ElevatorCfg s/closest-stopped-or-closest-moving-strategy 5 4))
       (pickup 1 4)
       (tick-view)
       (tick-view)
       (pickup 4 0)
       (tick-view)
       (pickup 0 1)
       (tick-view)
       (pickup 2 4)
       (tick-view)
       (pickup 4 3)
       (pickup 2 4)
       (tick-view-until-finished))


#_ (-> (make-elevator-sim (->ElevatorCfg s/closest-stopped-or-random-strategy 5 16))
    (pickup 2 4)
    (tick-view)
    (tick-view)
    (pickup 3 0)
    (tick-view)
    (tick-view)
    (pickup 4 1)
    (tick-view-until-finished))
