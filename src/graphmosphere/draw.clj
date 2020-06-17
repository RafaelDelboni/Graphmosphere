(ns graphmosphere.draw
  (:require [quil.core :as q]))

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
              :pos    {:x -150 :y -50 :z 0}}}]})

; pure logic fns
(defn calculate-echo-variation
  [pos var-keys var-size]
  (->> var-keys
       (mapv (fn [vertice]
               (-> pos
                   (update-in [vertice] #(+ var-size %))
                   (select-keys [vertice]))))
       (reduce merge pos)))

(defn update-echo-state
  [state base-key {:keys [var-keys var-size]}]
  (update state
          base-key
          #(calculate-echo-variation % var-keys var-size)))

; impure integration fns
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
  [func {:keys [stroke fill] {:keys [x y z]} :pos}]
  (draw-stroke stroke)
  (draw-fill fill)
  (q/with-translation [x y z] (func)))

(defn do-echo-draw
  [func {:keys [echo] :as draw}]
  (if-let [{:keys [iter]} echo]
    (loop [state draw iterations (dec iter)]
      (do-draw func state)
      (if-not (= iterations 0)
        (recur (update-echo-state state :pos echo)
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

(defn draw-shape
  [{:keys [type] :as shape}]
  (case type
    :sphere (draw-3d-sphere shape)
    :box (draw-3d-box shape)
    :quad (draw-2d-quad shape)
    :arc (draw-2d-arc shape)
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
