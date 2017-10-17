(ns pigs.core-test
  (:require [clojure.test :refer :all]
            [pigs.core :refer :all]))

(defn pigs [players]
  {:scores [0 0]})

(deftest pig-game-creation-test
  (testing "a game starts with the number of players passed in"
    (is (= 2 (count (:scores (pigs 2)))))))
