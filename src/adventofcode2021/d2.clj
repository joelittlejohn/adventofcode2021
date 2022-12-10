(ns adventofcode2021.d2
  (:require [clojure.java.io :as io]
            [clojure.string :as str]))

(def input
  (->> (io/resource "2.txt") slurp str/split-lines (map #(let [[d n] (str/split % #" ")] [d (Integer/valueOf n)]))))

(defn move
  [[h d] [m n]]
  (case m
    "forward" [(+ h n) d]
    "up" [h (- d n)]
    "down" [h (+ d n)]))

(->> (reduce move [0 0] input)
     (apply *))

;; => 1989265

(defn move2
  [[h d a] [m n]]
  (case m
    "forward" [(+ h n) (+ d (* a n)) a]
    "up" [h d (- a n)]
    "down" [h d (+ a n)]))

(let [[h d] (reduce move2 [0 0 0] input)]
  (* h d))

;; => 2089174012
