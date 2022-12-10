(ns adventofcode2021.d5
  (:require [clojure.java.io :as io]
            [clojure.string :as str]))

(def lines
  (->> (io/resource "5.txt") slurp str/split-lines (map (fn [l] (->> l (re-seq #"\d+") (map #(Integer/valueOf %)) (partition 2))))))

(defn straight?
  [[[x1 y1] [x2 y2]]]
  (or (= x1 x2) (= y1 y2)))

(defn plus
  [[x1 y1] [x2 y2]]
  [(+ x1 x2) (+ y1 y2)])

(defn points
  [[[x1 y1 :as a] [x2 y2 :as b]]]
  (let [v [(Integer/signum (- x2 x1)) (Integer/signum (- y2 y1))]]
    (conj (take-while #(not= b %) (iterate #(plus v %) a)) b)))

(->> lines
     (filter straight?)
     (mapcat points)
     (reduce #(update %1 %2 (fnil inc 0)) {})
     (filter #(<= 2 (second %)))
     count)
;; => 5167


(->> lines
     (mapcat points)
     (reduce #(update %1 %2 (fnil inc 0)) {})
     (filter #(<= 2 (second %)))
     count)
;; => 17604
