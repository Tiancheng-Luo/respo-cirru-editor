
(ns cirru-editor.main
  (:require [respo.core :refer [render! clear-cache! realize-ssr!]]
            [respo.cursor :refer [mutate]]
            [cirru-editor.comp.container :refer [comp-container]]
            [cljs.reader :refer [read-string]]
            [cirru-editor.util.dom :refer [focus!]]
            (cirru-editor.schema :as schema)))

(def ssr? (some? (.querySelector js/document "meta.respo-ssr")))

(defonce *store (atom schema/store))

(def *touched (atom false))

(defn dispatch! [op op-data]
  (comment println "dispatch:" op op-data)
  (case op
    :save (reset! *store op-data)
    :states (swap! *store update :states (mutate op-data))
    nil)
  (reset! *touched true))

(def mount-target (.querySelector js/document ".app"))

(defn render-app! [renderer]
  (renderer mount-target (comp-container @*store) dispatch!)
  (if @*touched (do (reset! *touched false) (println "changing focus") (focus!))))

(defn main! []
  (if ssr? (render-app! realize-ssr!))
  (render-app! render!)
  (add-watch *store :changes (fn [] (render-app! render!)))
  (println "app started!"))

(defn reload! [] (clear-cache!) (render-app! render!) (println "code updated."))

(set! js/window.onload main!)
