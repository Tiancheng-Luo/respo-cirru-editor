
(ns cirru-editor.render
  (:require [respo.alias :refer [html head title script style meta' div link body]]
            [respo.render.html :refer [make-html make-string]]
            [cirru-editor.comp.container :refer [comp-container]]
            ["fs" :refer [readFileSync writeFileSync]]
            (cirru-editor.schema :as schema)))

(defn spit [file-name content]
  (writeFileSync file-name content)
  (println "Wrote to:" file-name))

(def icon-url "http://repo.cirru.org/logo.cirru.org/cirru-400x400.png")

(defn html-dsl [resources html-content]
  (make-html
   (html
    {}
    (head
     {}
     (title {:innerHTML "Cirru Editor"})
     (link {:rel "icon", :type "image/png", :href icon-url})
     (link {:rel "manifest", :href "manifest.json"})
     (meta' {:charset "utf8"})
     (meta' {:name "viewport", :content "width=device-width, initial-scale=1"})
     (if (:build? resources) (meta' {:id "server-rendered", :type "text/edn"}))
     (if (contains? resources :css)
       (link {:rel "stylesheet", :type "text/css", :href (:css resources)})))
    (body
     {}
     (div {:class-name "app", :innerHTML html-content})
     (if (:build? resources)
       (script {:src (:vendor resources)})
       (script {:src (:cljs-main resources)}))
     (script {:src (:main resources)})))))

(defn generate-empty-html []
  (html-dsl {:build? false, :main "/main.js", :cljs-main "/browser/main.js"} ""))

(defn slurp [x] (readFileSync x "utf8"))

(defn generate-html []
  (let [tree (comp-container schema/store)
        html-content (make-string tree)
        resources (let [manifest (js/JSON.parse (slurp "dist/manifest.json"))]
                    {:build? true,
                     :css (aget manifest "main.css"),
                     :main (aget manifest "main.js"),
                     :vendor (aget manifest "vendor.js")})]
    (html-dsl resources html-content)))

(defn main! []
  (if (= js/process.env.env "dev")
    (spit "target/index.html" (generate-empty-html))
    (spit "dist/index.html" (generate-html))))

(main!)
