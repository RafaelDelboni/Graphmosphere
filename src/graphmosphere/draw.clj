(ns graphmosphere.draw
  (:require [quil.core :as q]))

(def test-temporary-state
  {:shapes
   [{:type   :sphere
     :radius 25
     :pos    {:x 50 :y 10 :z 0}
     :echo   {:iterations 1 :variation 10}}
    {:type   :sphere
     :radius 25
     :pos    {:x 10 :y 100 :z 0}
     :echo   {:iterations 1 :variation 10}}]})

(defn setup-state [state]
  (merge {} state))

(defn update-state [_state]
  test-temporary-state)

(defn draw-sphere
  [{:keys [radius] {:keys [x y z]} :pos}]
  (q/translate x y z)
  (q/sphere radius)
  (q/translate (- x) (- y) (- z)))

(defn draw-shape
  [{:keys [type] :as shape}]
  (case type
    :sphere (draw-sphere shape)
    (q/debug shape)))

(defn draw-state [state]
  (q/clear)
  (->> (:shapes state)
       (run! #(draw-shape %)))
  (q/camera 10 150 150 0 0 0 0 0 1)
  (q/rect 0 0 100 100)
  (q/translate 0 0 100)
  (q/box 70))

(defn export-draw-state [state]
  (draw-state state)
  (q/save "circle.png")
  (q/exit))

(defn on-close [state]
  (println ["closed" state]))
