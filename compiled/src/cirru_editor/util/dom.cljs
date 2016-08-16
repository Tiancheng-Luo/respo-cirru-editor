
(ns cirru-editor.util.dom)

(defn focus! []
  (js/setTimeout
    (fn []
      (let [editor-focus (.querySelector js/document "#editor-focused")
            current-focus (.-activeElement js/document)]
        (if (some? editor-focus)
          (if (not= editor-focus current-focus)
            (.focus editor-focus)
            nil)
          (println "Editor warning: cannot find focus target."))))))
