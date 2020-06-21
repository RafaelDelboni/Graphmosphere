(ns graphmosphere.draw
  (:require [graphmosphere.logic.draw :as logic]
            [quil.core :as q]))

(def test-temporary-state
  {:shapes
   [{:type   :sphere
     :shape  {:radius 25 :detail 50}
     :draw   {:fill   {:r 255 :g 255 :b 255 :a 255}
              :pos    {:x 100 :y 10 :z 10}}}
    {:type   :quad
     :shape  {:x1 0 :y1 0 :x2 0 :y2 1 :x3 1 :y3 1 :x4 1 :y4 0}
     :draw   {:fill   {:r 255 :g 255 :b 255 :a 255}
              :pos    {:x 0 :y 0 :z 0}}}
    {:type   :quad
     :shape  {:x1 0 :y1 0 :x2 0 :y2 1 :x3 1 :y3 1 :x4 1 :y4 0}
     :draw   {:fill   {:r 255 :g 255 :b 255 :a 255}
              :pos    {:x -200 :y 0 :z 0}}}
    {:type   :quad
     :shape  {:x1 0 :y1 0 :x2 0 :y2 1 :x3 1 :y3 1 :x4 1 :y4 0}
     :draw   {:fill   {:r 255 :g 255 :b 255 :a 255}
              :pos    {:x 200 :y 0 :z 0}}}
    {:type   :quad
     :shape  {:x1 0 :y1 0 :x2 0 :y2 1 :x3 1 :y3 1 :x4 1 :y4 0}
     :draw   {:fill   {:r 255 :g 255 :b 255 :a 255}
              :pos    {:x 0 :y -110 :z 0}}}
    {:type   :quad
     :shape  {:x1 0 :y1 0 :x2 0 :y2 1 :x3 1 :y3 1 :x4 1 :y4 0}
     :draw   {:fill   {:r 255 :g 255 :b 255 :a 255}
              :pos    {:x 0 :y 110 :z 0}}}
    {:type   :box
     :shape  {:width 10 :height 10 :depth 10}
     :draw   {:fill   {:r 255 :g 255 :b 255 :a 255}
              :stroke {:r 0 :g 0 :b 0 :a 255}
              :pos    {:x 150 :y 100 :z 0}
              :echo   {:iter 20 :var-size -15 :var-keys [:x :z]}}}
    {:type   :arc
     :shape  {:width 50 :height 50 :start 0 :stop q/QUARTER-PI :mode :pie}
     :draw   {:fill   {:r 255 :g 255 :b 255 :a 255}
              :stroke {:r 255 :g 0 :b 0 :a 255 :weight 3}
              :pos    {:x -150 :y -50 :z 0}}}
    {:type   :ellipse
     :shape  {:width 5 :height 15}
     :draw   {:fill   {:r 255 :g 255 :b 255 :a 255}
              :pos    {:x 50 :y -50 :z 0 :angle q/QUARTER-PI}}}
    {:type   :line
     :shape  {:x1 0 :y1 0 :z1 0 :x2 10 :y2 10 :z2 0}
     :draw   {:stroke {:r 0 :g 0 :b 255 :a 255}
              :pos    {:x -50 :y 25 :z 0}}}
    {:type   :point
     :shape  {:x 0 :y 0 :z 0}
     :draw   {:stroke {:r 255 :g 255 :b 255 :a 255 :weight 5}
              :pos    {:x -50 :y -50 :z 0}}}
    {:type   :triangle
     :shape  {:x1 0 :y1 0 :x2 5 :y2 10 :x3 10 :y3 0}
     :draw   {:stroke {:r 255 :g 255 :b 255 :a 255}
              :pos    {:x 0 :y -50 :z 0}}}
    {:type   :rect
     :shape  {:x 0 :y 0 :width 10 :height 5 :top-left-r 0 :top-right-r 0 :bottom-right-r 0 :bottom-left-r 0}
     :draw   {:stroke {:r 255 :g 255 :b 255 :a 255}
              :fill   {:r 125 :g 125 :b 125 :a 255}
              :pos    {:x 100 :y -100 :z 0}}}]})

(defn setup-state [state]
  (merge {} state))

(defn update-state [_state]
  test-temporary-state)

(defn draw-stroke
  [{:keys [r g b a weight] :as stroke}]
  (q/stroke-weight (or weight 1))
  (if stroke
    (q/stroke r g b a)
    (q/no-stroke)))

(defn draw-fill
  [{:keys [r g b a] :as fill}]
  (if fill
    (q/fill r g b a)
    (q/no-fill)))

(defn do-draw
  [func {:keys [stroke fill] {:keys [x y z angle]} :pos}]
  (draw-stroke stroke)
  (draw-fill fill)
  (q/with-translation [x y z]
    (q/with-rotation [(or angle 0)]
      (func))))

(defn do-echo-draw
  [func {:keys [echo] :as draw}]
  (if-let [{:keys [iter]} echo]
    (loop [state draw iterations (dec iter)]
      (do-draw func state)
      (if-not (= iterations 0)
        (recur (logic/update-echo-state state :pos echo)
               (dec iterations))))
    (do-draw func draw)))

(defn draw-func-with-args
  [func draw]
  (do-echo-draw func draw))

(defn draw-3d-sphere
  [{:keys [shape draw]}]
  (q/sphere-detail (or (:detail shape) 30))
  (draw-func-with-args #(q/sphere (:radius shape)) draw))

(defn draw-3d-box
  [{:keys [draw] {:keys [width height depth]} :shape}]
  (draw-func-with-args #(q/box width height depth) draw))

(defn draw-2d-quad
  [{:keys [draw] {:keys [x1 y1 x2 y2 x3 y3 x4 y4]} :shape}]
  (draw-func-with-args #(q/quad x1 y1 x2 y2 x3 y3 x4 y4) draw))

(defn draw-2d-arc
  [{:keys [draw] {:keys [width height start stop mode]} :shape}]
  (draw-func-with-args #(q/arc 0 0 width height start stop mode) draw))

(defn draw-2d-ellipse
  [{:keys [draw] {:keys [width height]} :shape}]
  (draw-func-with-args #(q/ellipse 0 0 width height) draw))

(defn draw-2d-line
  [{:keys [draw] {:keys [x1 y1 z1 x2 y2 z2]} :shape}]
  (draw-func-with-args #(q/line x1 y1 z1 x2 y2 z2) draw))

(defn draw-2d-point
  [{:keys [draw] {:keys [x y z]} :shape}]
  (draw-func-with-args #(q/point x y z) draw))

(defn draw-2d-triangle
  [{:keys [draw] {:keys [x1 y1 x2 y2 x3 y3]} :shape}]
  (draw-func-with-args #(q/triangle x1 y1 x2 y2 x3 y3) draw))

(defn draw-2d-rect
  [{:keys [draw]
    {:keys [x y width height top-left-r top-right-r bottom-right-r bottom-left-r]} :shape}]
  (draw-func-with-args #(q/rect x y width height top-left-r top-right-r bottom-right-r bottom-left-r) draw))

(defn draw-shape
  [{:keys [type] :as shape}]
  (case type
    :sphere (draw-3d-sphere shape)
    :box (draw-3d-box shape)
    :quad (draw-2d-quad shape)
    :arc (draw-2d-arc shape)
    :ellipse (draw-2d-ellipse shape)
    :line (draw-2d-line shape)
    :point (draw-2d-point shape)
    :triangle (draw-2d-triangle shape)
    :rect (draw-2d-rect shape)
    (q/debug shape)))

(defn draw-state [state]
  (q/camera 0 1 200 0 0 0 0 0 1)
  (q/clear)
  (->> (:shapes state)
       (run! #(draw-shape %))))

(defn export-draw-state [state]
  (draw-state state)
  (q/save "image.png")
  (q/exit))

(defn on-close [state]
  (println ["closed" state]))
