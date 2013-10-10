(ns wrecking-ball.core
  (:require [clojure.java.io         :refer [file]]
            [wrecking-ball.load-path :refer [read-file-from-load-path]]))

(def rendering-engines [{:name :hiccup :extensions ["hiccup" "hiccup.clj"]}
                        {:name :fleet :extensions ["html.fleet" "fleet"]}])

(def extensions (vec (mapcat :extensions rendering-engines)))

(def ^{:dynamic true
       :doc "Var that holds a map with all the information required to render a page."}
  *view-context*
  {:load-paths []
   :helpers []})

(defmacro with-updated-context [kwargs & body]
  `(binding [*view-context* (merge *view-context* ~kwargs)]
    ~@body))

(def ns-for-template
  (memoize
    (fn [template-name]
      (let [view-ns (create-ns (gensym (str "wrecking-ball.views-" template-name)))]
        (binding [*ns* view-ns]
          (use 'clojure.core)
          (use 'wrecking-ball.core)
          view-ns)))))

(defmacro in-template-ns [template-name & body]
  `(let [view-ns# (ns-for-template ~template-name)]
    (binding [*ns* view-ns#]
      ~@body)))

(def ^:private render-fn-for-engine
  (memoize
    (fn [engine-name]
      (let [ns-sym (symbol (str "wrecking-ball." (name engine-name)))]
        (require ns-sym)
        (ns-resolve (the-ns ns-sym) 'render)))))

(defn- rendering-engine-for-extension [extension]
  (some
    (fn [{:keys [extensions] :as engine}]
      (some #(when (= % extension) (:name engine)) extensions))
    rendering-engines))

(defn- render [{:keys [body extension template-name] :as template}]
  (-> extension
    rendering-engine-for-extension
    render-fn-for-engine
    (as-> render-fn
      (in-template-ns template-name (render-fn body)))))

(def endl (System/getProperty "line.separator"))

(defn- throw-template-not-found [template-name]
  (let [not-found-message (str "Template Not Found: " template-name)
        extensions-message (str "extensions: " (str extensions))
        load-paths-message (str "load path:  " (:load-paths *view-context*))
        message (clojure.string/join endl [not-found-message extensions-message load-paths-message])]
    (throw (Exception. message))))

(defn- read-template [template-name]
  (if-let [f (read-file-from-load-path template-name (:load-paths *view-context*) extensions)]
    (assoc f :template-name template-name)
    (throw-template-not-found template-name)))

(defn- render-in-layout [body]
  (if-let [layout (:layout *view-context*)]
    (binding [*view-context* (assoc *view-context* :template-body body)]
      (-> layout read-template render))
    body))

(defn render-template
  "Expects the location of a template and any optional parameters. Returns the
  rendered html of the specified template. Also adds any parameters and
  their values to the *view-context*."
  [template & {:as kwargs}]
  (with-updated-context kwargs
    (-> template
      read-template
      render
      render-in-layout)))
