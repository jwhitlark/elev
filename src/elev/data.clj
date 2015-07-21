(ns elev.data
  "Primary data structures for the sim."
  (:require [elev.data.impl :as impl]
            [elev.data.query :as q]
            [elev.util :refer [visual-direction]]
            [elev.abstract :refer :all]
            [table.core :refer [table]]))


(defrecord ElevatorCfg [strategy max-floors elevator-cnt])
(defrecord Elevator [id cur-flr dest dir])
(defrecord ElevatorSim [cfg elevators step])

(defn make-elevator [id]
  (->Elevator id 0 #{} :stopped))

(defn make-elevator-sim [cfg]
  (assert (> (:max-floors cfg) 0) "Can't have zero or less floors.")
  (assert (> (:elevator-cnt cfg) 0) "Can't have zero or less Elevators..")
  (->ElevatorSim cfg (mapv make-elevator (range (:elevator-cnt cfg))) 0))


(defn- visualize-floor [sim floor]
  (->> (:elevators sim)
       (mapv #(if (q/on-floor? % floor) (visualize %) ""))
       (cons floor)))

(extend-protocol Mover
     Elevator
     (tick [this]
       (let [new-flr (impl/update-floor this)
             new-goal (impl/update-goal this new-flr)
             new-direction (impl/update-direction this new-goal new-flr)]
         ;; TODO: Callbacks can go here (jw 15-07-21)
         (->Elevator (:id this) new-flr new-goal new-direction)))

     (visualize [this]
       (visual-direction (:dir this)))

     (pickup [this start-floor end-floor]
       (let [current-floor (:cur-flr this)
             addl-floors (if (= current-floor start-floor)
                           [end-floor]
                           [start-floor end-floor])
             new-goal (conj (:dest this) addl-floors)]
         (println (format "Elevator %d has queued a pickup on %d and a drop off on %d." (:id this) start-floor end-floor))
         (->Elevator (:id this) current-floor new-goal (impl/update-direction this new-goal current-floor))))

     ElevatorSim
     (tick [this]
       (println "tick")
       (->ElevatorSim
        (:cfg this)
        (mapv tick (:elevators this))
        (inc (:step this))))

     (visualize [this]
       (let [elevators (->> this :elevators count)
             header (->> elevators range (cons (format "t: %3d" (:step this))))
             floors (->> this :cfg :max-floors range reverse)]
         (table (into [header]  (mapv #(visualize-floor this %) floors)))
         (println (format "Remaining pickups: %s" (-> this :elevators q/all-pickups)))
         (println (format "Remaining dropoffs: %s" (-> this :elevators q/all-deliveries)))
         this))

     (pickup [this cur-flr dest-flr]
       (let [strategy-fn (get-in this [:cfg :strategy])
             selected-elev-index (strategy-fn (:elevators this) cur-flr dest-flr)
             selected-elev (nth (:elevators this) selected-elev-index)]
         (->ElevatorSim
          (:cfg this)
          (assoc (:elevators this) selected-elev-index (pickup selected-elev cur-flr dest-flr))
          (:step this)))))


(extend-protocol Sim
  ElevatorSim
  (status [this]
    (:elevators this)))


(defn tick-view [sim]
  (-> sim tick visualize))

(defn tick-view-until-finished [sim]
  (when-not (-> sim :elevators q/all-stopped?)
    (recur (tick-view sim))))
