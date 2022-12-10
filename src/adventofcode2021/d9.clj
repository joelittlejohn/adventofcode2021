(ns adventofcode2021.d9
  (:require [clojure.java.io :as io]
            [clojure.string :as str]))

(def input
  (->> (io/resource "9.txt") slurp str/split-lines (mapv (fn [line] (mapv #(Integer/valueOf (str %)) line)))))

(defn- neighbours
  [grid [x y]]
  (for [nx (range (dec x) (+ 2 x))
        ny (range (dec y) (+ 2 y))
        :when (and (nat-int? nx) (nat-int? ny)
                   (< nx (count (first grid)))
                   (< ny (count grid))
                   (not= [x y] [nx ny])
                   (or (= x nx) (= y ny)))]
    [nx ny]))

(defn- height
  [grid [x y]]
  (get-in grid [y x]))

(defn- low-points
  [grid]
  (for [x (range (count (first grid)))
        y (range (count grid))
        :let [h (height grid [x y])
              neighbour-heights (->> (neighbours grid [x y]) (map #(height grid %)) sort)]
        :when (< h (first neighbour-heights))]
    [x y]))

(->> input low-points (map (fn [p] (inc (height input p)))) (reduce +))
;; => 498

(defn- calculate-basin
  ([grid pos]
   (calculate-basin #{pos} grid pos))
  ([basin grid pos]
   (let [h (height grid pos)
         new-neighbours-within-basin (->> (neighbours grid pos)
                                          (remove basin)
                                          (remove #(= 9 (height grid %)))
                                          (filter #(> (height grid %) h)))]
     (if (seq new-neighbours-within-basin)
       (reduce #(apply conj %1 (calculate-basin %1 grid %2))
               (apply conj basin new-neighbours-within-basin)
               new-neighbours-within-basin)
       basin))))

(->> (low-points input)
     (map #(calculate-basin input %))
     (map count)
     sort
     reverse
     (take 3)
     (reduce *))
;; => 1071000
