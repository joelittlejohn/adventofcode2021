(ns adventofcode2021.d6
  (:require [clojure.java.io :as io]
            [clojure.string :as str]))

(def initial-state
  (map #(Integer/valueOf (str/trim %)) (-> (io/resource "6.txt") slurp (str/split #","))))

(defn- tick
  [state]
  (reduce #(if (zero? %2)
               (conj %1 6 8)
               (conj %1 (dec %2))) [] state))

(-> (iterate tick initial-state)
    (nth 80)
    count)
;; => 387413


(def age->count
  (memoize
   (fn [age days]
     (cond
       (zero? days) 1
       (pos? age) (recur (dec age) (dec days))
       :else (+ (age->count 6 (dec days)) (age->count 8 (dec days)))))))

(->> initial-state
     (map #(age->count % 256))
     (reduce +))
;; => 1738377086345
