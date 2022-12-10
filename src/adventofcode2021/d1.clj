(ns adventofcode2021.d1
  (:require [clojure.java.io :as io]
            [clojure.string :as str]
            [net.cgrand.xforms :as xforms]))

(def input
  (->> (io/resource "1.txt") slurp str/split-lines (map #(Integer/valueOf %))))

(->> input
     (partition 2 1)
     (filter #(apply < %))
     count)
;; => 1154

(->> input
     (partition 3 1)
     (map #(apply + %))
     (partition 2 1)
     (filter #(apply < %))
     count)
;; => 1127


;; transducer version
(sequence (comp (xforms/partition 2 1) (filter #(apply < %)) xforms/count) input2)
