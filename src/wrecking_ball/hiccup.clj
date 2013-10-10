(ns wrecking-ball.hiccup
  (:require [hiccup.core :refer [html]]))

(defn render [body]
  (html (load-string body)))
