(ns elev.data.impl
  "Implementaion details for elev.data. Should not be used elsewhere."
  (:require [elev.data.query :as q]))

(defn update-floor
  "Calculate the new floor based on direction."
  [{current-floor :cur-flr direction :dir}]
  (case direction
    :up (inc current-floor)
    :down (dec current-floor)
    :stopped current-floor))

(defn- flatten-set [st]
  (-> st vec flatten set))

(defn- remove-start
  "If the pickup floor matches the current floor, remove it and just
  return the delivery floor."
  [destination floor]
  (if (= floor (first destination))
    (rest destination)
    destination))

(defn update-goal
  "Remove the current-floor from the destination set."
  [{destinations :dest} floor]
  (-> destinations
      (disj [floor]) ;; remove end floor
      (->> (map #(remove-start % floor))) ;; remove start floor
      set))

(defn wants-up?
  "Are there goals above the current floor?"
  [goal floor]
  (->> goal flatten-set
       (filter #(> % floor))
       empty?
       not))

(defn wants-down?
  "Are there goals below the current floor?"
  [goal floor]
  (->> goal flatten-set
       (filter #(< % floor))
       empty?
       not))

(defn update-direction
  "Update direction based on current direction and remaining
  goals.  (i.e. this doesn't change direction until it's satisfied
  all its goals in the current direction.)"
  [elevator goal floor]
  (cond
    (empty? goal)                  :stopped
    (and (q/going-up? elevator)
         (wants-up? goal floor))   :up
    (and (q/going-down? elevator)
         (wants-down? goal floor)) :down
    (wants-up? goal floor)         :up
    (wants-down? goal floor)       :down
    :else                          :stopped))
