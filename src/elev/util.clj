(ns elev.util)

(def UP "▲")
(def DOWN "▼")
(def STOPPED "■")

(defn visual-direction [direction]
  (case direction
    :up UP
    :down DOWN
    :stopped STOPPED))
