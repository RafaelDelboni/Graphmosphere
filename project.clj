(defproject graphmosphere "0.1.0-SNAPSHOT"
  :description "A Twitter Bot that create geometric pictures and gifs from static noises."
  :url "https://twitter.com/graphmosphere"
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url "https://www.eclipse.org/legal/epl-2.0/"}
  :dependencies [[org.clojure/clojure "1.10.1"]
                 [quil "3.1.0"]]
  :main ^:skip-aot graphmosphere.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}}
  :uberjar-name "graphmosphere.jar")
