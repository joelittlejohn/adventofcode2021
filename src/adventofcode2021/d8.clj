(ns adventofcode2021.d8
  (:require [clojure.java.io :as io]
            [clojure.string :as str]
            [clojure.set :as set]))

(def input
  (for [line (->> (io/resource "8.txt") slurp str/split-lines)
        :let [[patterns output] (str/split line #"\ \|\ ")]]
    [(str/split patterns #" ") (str/split output #" ")]))

(def n-segments
  [6 2 5 5 4 5 6 3 7 6])

(def unique-length?
  (set (map #(n-segments %) [1 4 7 8])))

(->> input
     (mapcat second)
     (filter #(unique-length? (count %)))
     count)
;; => 493

(def counts->segment
  {#{3 5 6 7} #{\a}
   #{4 5 6 7} #{\b \d}
   #{2 3 4 5 6 7} #{\c \f}
   #{5 6 7} #{\e \g}})

(def segment->numbers
  {\a #{0 2 3 5 6 7 8 9}
   \b #{0 4 5 6 8 9}
   \c #{0 1 2 3 4 7 8 9}
   \d #{2 3 4 5 6 8 9}
   \e #{0 2 6 8}
   \f #{0 1 3 4 5 6 7 8 9}
   \g #{0 2 3 5 6 8 9}})

(def segments->number
  {#{\a \b \c \e \f \g} 0
   #{\c \f} 1
   #{\a \c \d \e \g} 2
   #{\a \c \d \f \g} 3
   #{\b \c \d \f} 4
   #{\a \b \d \f \g} 5
   #{\a \b \d \e \f \g} 6
   #{\a \c \f} 7
   #{\a \b \c \d \e \f \g} 8
   #{\a \b \c \d \f \g} 9})

(def appearances->segment
  {8 #{\a \c}
   6 #{\b}
   7 #{\d \g}
   4 #{\e}
   9 #{\f}})

(defn- possible-by-segment-count
  [patterns]
  (let [patterns-as-sets (map set patterns)]
    (->> (for [c [\a \b \c \d \e \f \g]]
           [c (->> patterns-as-sets (filter #(% c)) (map count) set counts->segment)])
         (into {}))))

(defn- possible-by-number-of-appearances
  [patterns]
  (let [patterns-as-sets (map set patterns)]
    (->> (for [c [\a \b \c \d \e \f \g]]
           [c (->> patterns-as-sets (filter #(% c)) count appearances->segment set)])
         (into {}))))

(defn- create-mapping
  [patterns]
  (let [ps (possible-by-segment-count patterns)
        pn (possible-by-number-of-appearances patterns)]
    (->> (for [c [\a \b \c \d \e \f \g]
               :let [sc (ps c)
                     sn (pn c)]]
           [c (first (set/intersection sc sn))])
         (into {}))))

(defn- digits->number
  [digits]
  (->> digits
       reverse
       (zipmap (iterate #(* 10 %) 1))
       (map #(apply * %))
       (reduce +)))

(->> (for [[patterns output] input
           :let [m (create-mapping patterns)
                 digits (->> output (map #(segments->number (set (map m %)))))]]
       (digits->number digits))
     (reduce +))
;; => 1010460
