
(ns cirru-editor.comp.expression
  (:require [respo.alias :refer [create-comp div]]
            [cirru-editor.comp.token :refer [comp-token]]))

(declare comp-expression)

(defn render [expression]
  (fn [state mutate!]
    (div
      {}
      (->>
        expression
        (map-indexed
          (fn [idx item] [idx
                          (if (string? item)
                            (comp-token item)
                            (comp-expression item))]))))))

(def comp-expression (create-comp :expression render))