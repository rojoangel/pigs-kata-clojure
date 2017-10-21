(ns pigs.command-line
  (:require [pigs.core :as pigs]
            [clojure.string :as str]))

(defn -main [& args]
  (do
    (println "Let's play pigs...")
    (do
      (print "Please, enter number of players: ")
      (flush)
      (let [[players-arg & _] (str/split (read-line) #" ")]
        (read-string players-arg)))))
