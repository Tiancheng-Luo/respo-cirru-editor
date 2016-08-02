
(ns cirru-editor.modifier.tree)

(defn update-token [snapshot op-data]
  (let [[coord new-token] op-data]
    (-> snapshot (assoc-in (cons :tree coord) new-token))))

(defn after-token [snapshot op-data]
  (println "after token")
  (let [coord op-data]
    (-> snapshot
     (update-in
       (cons :tree (butlast coord))
       (fn [expression]
         (if (= (last coord) (dec (count expression)))
           (conj expression "")
           (into
             []
             (concat
               (subvec expression 0 (inc (last coord)))
               [""]
               (subvec expression (inc (last coord))))))))
     (update
       :focus
       (fn [coord]
         (conj (into [] (butlast coord)) (inc (last coord))))))))
