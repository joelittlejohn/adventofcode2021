(ns adventofcode2021.d3
  (:require [clojure.java.io :as io]
            [clojure.string :as str]))

(def input
  (->> (io/resource "3.txt") slurp str/split-lines))

(defn rate
  [comp in]
  (->> (apply map list in)
       (map #(->> % frequencies (sort-by second comp) ffirst))
       (apply str)))

(defn gamma-rate
  [in]
  (Integer/parseInt (rate > in) 2))

(defn epsilon-rate
  [in]
  (Integer/parseInt (rate < in) 2))

(* (gamma-rate input ) (epsilon-rate input))

;; => 2640986

(defn life-support-rating
  [comp in]
  (reduce
   (fn [in n]
     (if (= 1 (count in))
       (reduced (first in))
       (let [c (->> in (map #(.charAt % n)) frequencies (sort-by (juxt second first) comp) ffirst)]
         (filter #(= c (.charAt % n)) in))))
   in
   (range)))

(defn oxygen-rating
  [in]
  (Integer/parseInt (life-support-rating #(compare %2 %1) in) 2))

(defn co2-rating
  [in]
  (Integer/parseInt (life-support-rating compare in) 2))

(* (oxygen-rating input) (co2-rating input))

;; => 6822109
