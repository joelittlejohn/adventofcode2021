(ns adventofcode2021.d11
  (:require [clojure.java.io :as io]
            [clojure.string :as str]
            [clojure.walk :as walk]))

(def input
  (->> (io/resource "11.txt") slurp str/split-lines (mapv (fn [line] (mapv #(Integer/valueOf (str %)) line)))))

(defn- energy
  [grid [x y]]
  (get-in grid [y x]))

(defn- inc-energy
  [grid [x y]]
  (update-in grid [y x] inc))

(defn- inc-all
  [grid]
  (walk/postwalk #(if (number? %) (inc %) %) grid))

(defn- neighbours
  [grid [x y]]
  (for [nx (range (dec x) (+ 2 x))
        ny (range (dec y) (+ 2 y))
        :when (and (nat-int? nx) (nat-int? ny)
                   (< nx (count (first grid)))
                   (< ny (count grid))
                   (not= [x y] [nx ny]))]
    [nx ny]))

(defn- coordinates
  [grid]
  (for [x (range 0 (count (first grid)))
        y (range 0 (count grid))]
    [x y]))

(defn- step
  [grid]
  (loop [g (inc-all grid)]
    (if-let [flash (seq (filter #(> (energy grid %) 9) (coordinates grid)))]
      (recur (reduce inc-energy g flash))
      q)))
