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

(defn hold [game-state]
  (let [current-player-score-idx (dec (:player-turn game-state))
        rolls-total (apply + (:current-player-rolls game-state))]
    (update-in game-state [:scores current-player-score-idx] + rolls-total)))

(deftest holding-test
  (testing "holding after no rolls does nothing"
    (let [initial-game-state {:scores [1 2 3] :player-turn 1}]
      (is (= initial-game-state (hold initial-game-state) ))))
  (testing "holding adds rolls to current player score"
    (let [initial-game-state {:scores [1 2 3] :player-turn 1 :current-player-rolls [5 4 3]}]
      (is (= [13 2 3] (:scores (hold initial-game-state)))))
    (let [initial-game-state {:scores [1 2 3] :player-turn 2 :current-player-rolls [5 4 3]}]
      (is (= [1 14 3] (:scores (hold initial-game-state)))))))
