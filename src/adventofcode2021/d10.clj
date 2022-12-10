(ns adventofcode2021.d10
  (:require [clojure.java.io :as io]
            [clojure.string :as str]))

(def points
  {\) 3
   \] 57
   \} 1197
   \> 25137})

(def brackets
  {\( \)
   \[ \]
   \{ \}
   \< \>})

(def closing
  (set (vals brackets)))

(def input
  (-> (io/resource "10.txt") slurp str/split-lines))

(defn illegal-char
  [line]
  (peek (reduce #(cond (brackets %2) (conj %1 %2)
                       (and (closing %2) (= (brackets (peek %1)) %2)) (pop %1)
                       :else (reduced [%2])) [] line)))

(->> input
     (keep illegal-char)
     (keep points)
     (reduce +))
;; => 290691


(def completion-points
  {\) 1
   \] 2
   \} 3
   \> 4})

(defn completion
  [line]
  (let [stack (reduce #(cond (brackets %2) (conj %1 %2)
                             (and (closing %2) (= (brackets (peek %1)) %2)) (pop %1)) [] line)]
    (for [c (reverse stack)]
      (brackets c))))

(defn score
  [s]
  (reduce #(-> %1 (* 5) (+ (completion-points %2))) 0 s))

(let [scores (for [line input
                   :when (brackets (illegal-char line))]
               (-> line completion score))]
  (-> scores sort (nth (int (/ (count scores) 2)))))

;; => 2768166558
