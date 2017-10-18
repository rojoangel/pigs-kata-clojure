(ns pigs.core-test
  (:require [clojure.test :refer :all]
            [pigs.core :refer :all]))

(deftest pig-game-creation-test
  (testing "a game starts with the number of players passed in"
    (is (= 2 (count (:scores (new-game 2)))))
    (is (= 3 (count (:scores (new-game 3))))))
  (testing "all scores are zero when the game starts"
    (is (every? zero? (:scores (new-game 7)))))
  (testing "a game starts with player 1 turn"
    (is (= 1 (:player-turn (new-game 8)))))
  (testing "a game starts with no rolls"
    (is (empty? (:current-player-rolls (new-game 5))))))

(deftest holding-test
  (testing "holding after no rolls does not change the scores"
    (let [initial-game-state {:scores [1 2 3] :player-turn 1}]
      (is (= [1 2 3] (:scores (hold initial-game-state)) ))))
  (testing "holding adds rolls to current player score"
    (let [initial-game-state {:scores [1 2 3] :player-turn 1 :current-player-rolls [5 4 3]}]
      (is (= [13 2 3] (:scores (hold initial-game-state)))))
    (let [initial-game-state {:scores [1 2 3] :player-turn 2 :current-player-rolls [5 4 3]}]
      (is (= [1 14 3] (:scores (hold initial-game-state))))))
  (testing "holding changes the player turn"
    (let [initial-game-state {:scores [0 0 0] :player-turn 1}]
      (is (= 2 (:player-turn (hold initial-game-state)))))
    (let [initial-game-state {:scores [0 0 0] :player-turn 3}]
      (is (= 1 (:player-turn (hold initial-game-state))))))
  (testing "holding resets rolls"
    (let [initial-game-state {:scores [0 0 0] :player-turn 1 :current-player-rolls [4 5 4]}]
      (is (empty? (:current-player-rolls (hold initial-game-state)))))))

(deftest rolling-a-non-one-test
  (testing "rolling a value different to one adds dice value to rolls"
    (let [initial-game-state {:current-player-rolls '(4 5 4)}
          dice-value 2]
      (is (= dice-value (first (:current-player-rolls (roll initial-game-state dice-value)))))))
  (testing "rolling a value different to one keeps the player turn"
    (let [initial-game-state {:player-turn 1}
          dice-value 2]
      (is (= 1 (:player-turn (roll initial-game-state dice-value)))))))

(deftest rolling-a-one-test
  (testing "rolling a one resets rolls"
    (let [initial-game-state {:current-player-rolls '(4 6 5) :player-turn 1 :scores [0 0 0]}
          dice-value 1]
      (is (empty? (:current-player-rolls (roll initial-game-state dice-value))))))
  (testing "rolling a one changes the turn"
    (let [initial-game-state {:player-turn 1 :scores [0 0 0]}
          dice-value 1]
      (is (= 2 (:player-turn (roll initial-game-state dice-value)))))))

(deftest ending-the-game-test
  (testing "the game continues if all scores are less than 100"
    (let [game-state {:scores [99 0 12 78]}]
      (is (false? (end-game? game-state)))))
  (testing "the game ends if a score is equal or greater than 100"
    (let [game-state {:scores [100 0 12 78]}]
      (is (true? (end-game? game-state))))
    (let [game-state {:scores [0 12 103]}]
      (is (true? (end-game? game-state))))))

(defn winner [game-state]
  )

(deftest determining-the-winner-test
  (testing "nobody wins if there is no score greater than 100"
    (let [game-state {:scores [0 12 55 99 67]}]
      (is (nil? (winner game-state))))))