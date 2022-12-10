(ns adventofcode2021.d4
  (:require [clojure.java.io :as io]
            [clojure.string :as str]))

(def draw
  (map #(Integer/valueOf %) (-> (io/resource "4.txt") slurp str/split-lines first (str/split #","))))

(defn- parse-board
  [lines]
  (mapv (fn [l] (->> l (re-seq #"\d+") (mapv #(Integer/valueOf %)))) lines))

(defn- boards-seq
  [lines]
  (lazy-seq
   (when-let [board-lines (seq (take-while #(not= "" %) lines))]
     (cons (parse-board board-lines)
           (boards-seq (drop (inc (count board-lines)) lines))))))

(def boards
  (->> (io/resource "4.txt") slurp str/split-lines (drop 2) boards-seq))

(defn- won?
  [drawn? b]
  (boolean
   (or (some
        (fn [y]
          (every? #(drawn? (get-in b [y %])) (range (count (first b)))))
        (range (count b)))
       (some
        (fn [x]
          (every? #(drawn? (get-in b [% x])) (range (count b))))
        (range (count (first b)))))))

(defn- score
  [draw b]
  (->> (for [y (range (count b))
             x (range (count (first b)))]
         (get-in b [y x]))
       (remove (set draw))
       (apply +)
       (* (last draw))))

(->> draw
     (reductions #(conj %1 %2) [])
     (some (fn [draw]
             (let [drawn? (set draw)]
               (when-let [winner (some #(when (won? drawn? %) %) boards)]
                 (score draw winner))))))
;; => 49860

(let [draws (reductions #(conj %1 %2) [] draw)]
  (->> (for [board boards
             :let [winning-draw (some #(when (won? (set %) board) %) draws)]
             :when winning-draw]
         [winning-draw board])
       (sort-by #(count (first %)))
       last
       (apply score)))
;; => 24628
