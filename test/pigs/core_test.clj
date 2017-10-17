(ns pigs.core-test
  (:require [clojure.test :refer :all]
            [pigs.core :refer :all]))

(defn pigs [players]
  {
   :scores (repeat players 0)
   :player-turn 1
   })

(deftest pig-game-creation-test
  (testing "a game starts with the number of players passed in"
    (is (= 2 (count (:scores (pigs 2)))))
    (is (= 3 (count (:scores (pigs 3))))))
  (testing "all scores are zero when the game starts"
    (is (every? zero? (:scores (pigs 7)))))
  (testing "a game starts with player 1 turn"
    (is (= 1 (:player-turn (pigs 8)))))
  (testing "a game starts with no rolls"
    (is (empty? (:current-player-rolls (pigs 5))))))
