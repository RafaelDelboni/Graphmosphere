(ns graphmosphere.core
  (:require [quil.core :as q]
            [quil.middleware :as m]
            [graphmosphere.draw :as draw])
  (:gen-class))

(defn -main [& args]
  (q/sketch
    :title "graphmosphere"
    :renderer :p3d
    :size [500 500]
    :setup #(draw/setup-state {})
    :update #'draw/update-state
    :draw #'draw/draw-state
    :on-close #'draw/on-close
    :features [:keep-on-top]
    :middleware [m/fun-mode]))
