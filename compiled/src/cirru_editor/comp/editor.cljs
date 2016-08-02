
(ns cirru-editor.comp.editor
  (:require [hsl.core :refer [hsl]]
            [respo.alias :refer [create-comp div]]
            [respo.comp.debug :refer [comp-debug]]
            [cirru-editor.modifier.core :refer [updater]]
            [cirru-editor.comp.expression :refer [comp-expression]]))

(def style-editor
 {:background-color (hsl 200 10 40), :padding "8px 8px"})

(defn init-state [tree] {:tree tree, :focus []})

(defn update-state [state op op-data]
  (js/requestAnimationFrame
    (fn [timestamp]
      (let [editor-focus (.querySelector js/document "#editor-focused")
            current-focus (.-activeElement js/document)]
        (if (not= editor-focus current-focus) (.focus editor-focus)))))
  (updater state op op-data))

(defn render [snapshot]
  (fn [state mutate!]
    (div
      {:style style-editor}
      (comp-expression (:tree state) mutate! [] 0 false (:focus state))
      (comp-debug state nil))))

(def comp-editor (create-comp :editor init-state update-state render))
