(ns elev.data.query
  "Tools to operate on elevators or groups of elevators.")

(defn- pair [x] (= 2 (count x)))
(defn- empty-or-nil? [x] (or (nil? x) (empty? x)))
(defn- get-start-floor [x] (if (pair x) (first x)))
(defn- get-end-floor   [x] (if (pair x) (second x) (first x)))

;; Operate on a single Elevator.
;; --------------------------------------------------------------------------------

(defn going-up?   [{direction :dir}] (= direction :up))
(defn going-down? [{direction :dir}] (= direction :down))
(defn stopped?    [{direction :dir}] (= direction :stopped))

(defn on-floor?    [{current-floor :cur-flr} floor] (= current-floor floor))
(defn above-floor? [{current-floor :cur-flr} floor] (< current-floor floor))
(defn below-floor? [{current-floor :cur-flr} floor] (> current-floor floor))

(defn moving-towards-floor? [elevator floor]
  (or (and (above-floor? elevator floor) (going-down? elevator))
      (and (below-floor? elevator floor) (going-up? elevator))))

(defn distance-to-floor [elevator floor] (Math/abs (- floor (:cur-flr elevator))))

(defn pickups [{destinations :dest}]
  (map get-start-floor destinations))

(defn deliveries [{destinations :dest}]
  (map get-end-floor destinations))

;; Operate on a group of Elevators.
;; --------------------------------------------------------------------------------

(defn all-stopped? [elevators] (every? stopped? elevators))

(defn all-pickups    [elevators] (->> elevators (mapcat pickups) (remove nil?) set))
(defn all-deliveries [elevators] (->> elevators (mapcat deliveries) set))

(defn only-moving-towards-floor [elevators floor]
  (filter #(moving-towards-floor? % floor) elevators))

(defn group-by-distance [elevators floor]
  (group-by #(distance-to-floor % floor) elevators))

(defn score-by-distance [elevators floor]
  (->> elevators
       (remove nil?)
       (sort-by #(distance-to-floor % floor))))

(defn- closest
  "Select the closest"
  [elevators floor]
  (-> elevators
      (group-by-distance floor)
      sort first val rand-nth))

(defn closest-stopped
  "Select randomly from among the closest, stopped elevators."
  [elevators floor]
  (when-let [stopped (seq (filter stopped? elevators))]
    (closest stopped floor)))

(defn closest-moving
  "Closest Elevator moving towards floor. (random if more than one closest.)"
  [elevators floor]
  (when-let [moving (seq (only-moving-towards-floor elevators floor))]
    (closest moving floor)))
