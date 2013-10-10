(defproject wrecking-ball "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}

  :dependencies [[org.clojure/clojure "1.5.1"]
                 [com.cemerick/pomegranate "0.2.0"]]

  :profiles {:dev {:dependencies [[speclj "2.7.5"]
                                  [hiccup "1.0.2"]
                                  [fleet "0.10.1"]]
                   :main speclj.main
                   :aot [speclj.main]
                   :plugins [[speclj "2.7.5"]]
                   :source-paths ["spec" "src"]
                   :test-paths ["spec"]}}

  )
