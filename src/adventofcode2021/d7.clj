(ns adventofcode2021.d7
  (:require [clojure.java.io :as io]
            [clojure.string :as str]))

(def positions
  (map #(Integer/valueOf (str/trim %)) (-> (io/resource "7.txt") slurp (str/split #","))))

(->> (for [n (range (last (sort positions)))]
       [n (reduce + (map #(Math/abs (- % n)) positions))])
     (sort-by second)
     first
     second)
;; => 326132

(defn fuel
  [from to]
  (let [n (Math/abs (- from to))]
    (int (/ (+ (Math/pow n 2) n) 2))))

(time
 (->> (for [n (range (last (sort positions)))]
        [n (reduce + (map #(fuel % n) positions))])
      (sort-by second)
      first
      second))
;; => 88612508
