(ns pigs.command-line
  (:require [pigs.core :as pigs]
            [clojure.string :as str]))

(defn- salute []
  (println "Let's play pigs..."))

(defn- read-number-of-players []
  (do
    (print "Please, enter number of players: ")
    (flush)
    (let [[players-arg & _] (str/split (read-line) #" ")]
      (read-string players-arg))))

(defn -main [& args]
  (do
    (salute)
    (-> (read-number-of-players)
        (pigs/new-game)
        )))
