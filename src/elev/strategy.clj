(ns elev.strategy
  "The available strategies."
  (:require [elev.data.query :as q]))

;; A strategy accepts a sequence of elevators, the pickup floor and
;; the destination floor.  From that it returns the zero based index
;; of the elevator, (which is equivelant to its id).

(defn random-strategy
  "Resonably effective under light load.  Done for testing purposes."
  [elevators pickup-floor destination-floor]
  (rand-int (count elevators)))

(defn closest-stopped-or-random-strategy
  "Find the nearest not-in-use elevator, or a random one if all are in use."
  [elevators pickup-floor destination-floor]
  (let [stopped (q/closest-stopped elevators pickup-floor)]
    (or (:id stopped)
        (random-strategy elevators pickup-floor destination-floor))))

(defn closest-stopped-or-closest-moving-strategy
  "Good general strategy, Finds closest elevator that is not-in-use or
  is moving in the correct direction.

  Variations might include scoring stopped and moving elevators
  differently, or also taking the destination floor into account
  instead of just the pickup floor."
  [elevators pickup-floor destination-floor]
  (let [stopped (q/closest-stopped elevators pickup-floor)
        moving (q/closest-moving elevators pickup-floor)]
    (or (-> [stopped moving] (q/score-by-distance pickup-floor) first :id)
        (random-strategy elevators pickup-floor destination-floor))))
