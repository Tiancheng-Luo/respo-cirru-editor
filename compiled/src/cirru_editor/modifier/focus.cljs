
(ns cirru-editor.modifier.focus)

(defn focus-to [snapshot op-data]
  (let [coord op-data] (assoc snapshot :focus coord)))
