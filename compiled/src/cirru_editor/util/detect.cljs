
(ns cirru-editor.util.detect
  (:require [clojure.string :refer [includes?]]))

(defn deep? [expression] (some (fn [item] (vector? item)) expression))

(defn coord-contains? [a b]
  (if (nil? a)
    false
    (if (pos? (count b))
      (if (pos? (count a))
        (if (= (first a) (first b)) (recur (rest a) (rest b)) false)
        false)
      true)))

(defn has-blank? [x] (includes? x " "))
