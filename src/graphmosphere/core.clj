(ns graphmosphere.core
  (:require [quil.core :as q]
            [quil.middleware :as m]
            [graphmosphere.draw :as draw])
  (:gen-class))

(defn -main [& args]
  (q/sketch
  :title "You spin my circle right round"
  :renderer :p3d
  :size [500 500]
  :setup #(draw/setup-state {:a "a"})
  :draw #'draw/draw-state
  :on-close #'draw/on-close
  :features [:keep-on-top]
  :middleware [m/fun-mode]))
