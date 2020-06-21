(ns graphmosphere.logic.draw-test
  (:require [clojure.test :refer [deftest testing are is]]
            [graphmosphere.logic.draw :as logic]))

(def state {:pos {:x 0 :y 0 :z 0}})
(defn mk-echo [vsize vkeys] {:var-size vsize :var-keys vkeys})

(deftest logic-draw-test
  (testing logic/update-echo-state
    (are [result vsize vkeys] (= result (logic/update-echo-state state :pos (mk-echo vsize vkeys)))
      {:pos {:x 10 :y 0 :z 0}}   10 [:x]
      {:pos {:x 10 :y 10 :z 0}}  10 [:x :y]
      {:pos {:x 10 :y 10 :z 10}} 10 [:x :y :z]))
  (testing logic/calculate-echo-variation
    (are [result vsize vkeys] (= result (logic/calculate-echo-variation (:pos state) vkeys vsize))
      {:x 10 :y 0 :z 0}   10 [:x]
      {:x 10 :y 10 :z 0}  10 [:x :y]
      {:x 10 :y 10 :z 10} 10 [:x :y :z]))
  (testing logic/calculate-symmetrical-variations
    (are [result positions vkeys] (= result (logic/calculate-symmetrical-variations positions vkeys))
         [{:x 1 :y 1 :z 1}
          {:x -1 :y 1 :z 1}] [{:x 1 :y 1 :z 1}] [:x]
         [{:x 1, :y 1, :z 1}
          {:x -1, :y 1, :z 1}
          {:x 1, :y -1, :z 1}] [{:x 1 :y 1 :z 1}] [:x :y]))
  (testing logic/update-recursive-symmetrical-variations
    (are [result pos vkeys] (= result (logic/update-recursive-symmetrical-variations pos vkeys))
      [{:x 1 :y 1 :z 1}
       {:x -1 :y 1 :z 1}] {:x 1 :y 1 :z 1} [:x]
      [{:x 1, :y 1, :z 1}
       {:x 1, :y -1, :z 1}
       {:x -1, :y 1, :z 1}
       {:x -1, :y -1, :z 1}] {:x 1 :y 1 :z 1} [:x :y])))
