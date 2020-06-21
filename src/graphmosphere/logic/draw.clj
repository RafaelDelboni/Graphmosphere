(ns graphmosphere.logic.draw)

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

(defn calculate-symmetrical-variations
  [positions var-keys]
  (->> positions
       (mapcat (fn [pos] (->> var-keys
                              (mapv (fn [vertice]
                                      (-> pos (update-in [vertice] #(- %)))))
                              (into [pos]))))))

(defn update-recursive-symmetrical-variations
  "Calculate and update shape position state with an vector of positions if there
  is any symmetrical arguments in it's definition map.
  If var-keys is and vector with two keys, all resulting positions for the first
  element should be calculated for the next element exponentially."
  [pos var-keys]
  (reduce (fn [acc curr] (-> acc
                             (calculate-symmetrical-variations [curr])))
          [pos]
          var-keys))
