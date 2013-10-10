(ns wrecking-ball.hiccup
  (:require [hiccup.core :refer [html]]))

(defn render-hiccup [body]
  (html (load-string body)))
