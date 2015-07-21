(ns elev.abstract)

(defprotocol Mover
  (tick [this])
  (visualize [this])
  (pickup [this start-floor end-floor]))
;; need callback

(defprotocol Sim
  (status [this]))
