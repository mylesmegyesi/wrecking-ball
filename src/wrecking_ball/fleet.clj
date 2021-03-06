(ns wrecking-ball.fleet
  (:require [fleet         :refer [fleet]]
            [fleet.runtime :refer [Screenable raw]]))

(extend-protocol Screenable
  nil
  (screen [s f] (raw f "")))

(defn render [body]
  (str ((fleet [] body))))
