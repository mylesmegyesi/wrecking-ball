(ns wrecking-ball.mustache
  (:require [clostache.parser   :as    mustache]
            [wrecking-ball.core :refer [*view-context*]]))

(defn render [body]
  (mustache/render body *view-context*))
