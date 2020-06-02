(ns graphmosphere.draw
  (:require [quil.core :as q]))

(defn setup-state [state]
  (merge {:b "b"} state))

(defn draw-state [_state]
  (q/clear)
  (q/camera 150 150 150 0 0 0 0 0 1)
  (q/shininess 10)
  (q/sphere 50)
  (q/rect 0 0 100 100)
  (q/translate 0 0 100)
  (q/box 70))

(defn export-draw-state [state]
  (draw-state state)
  (q/save "circle.png")
  (q/exit))

(defn on-close [state]
  (println ["closed" state]))
