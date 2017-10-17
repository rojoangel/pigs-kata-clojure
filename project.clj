(defproject pigs "0.1.0-SNAPSHOT"
  :description "Pigs kata in clojure"
  :url "https://en.m.wikipedia.org/wiki/Pig_(dice_game)"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.7.0"]]
  :main ^:skip-aot pigs.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}}
  :plugins [[lein-auto "0.1.3"]])
