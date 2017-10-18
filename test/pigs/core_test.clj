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

(defn- add-rolls-to-current-player-score [game-state]
  (let [current-player-score-idx (dec (:player-turn game-state))
        rolls-total (apply + (:current-player-rolls game-state))]
    (update-in game-state [:scores current-player-score-idx] + rolls-total)))

(defn- next-turn [n max]
  (rem (inc n) max))

(defn- change-player-turn [game-state]
  (let [players (count (:scores game-state))]
    (update game-state :player-turn next-turn players)))

(defn- reset-rolls [game-state]
  (update game-state :current-player-rolls empty))

(defn hold [game-state]
  (-> game-state
      add-rolls-to-current-player-score
      reset-rolls
      change-player-turn))

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

(defn- add-to-rolls [game-state dice-value]
  (update game-state :current-player-rolls conj dice-value))

(defn roll [game-state dice-value]
  (if (= 1 dice-value)
    (reset-rolls game-state)
    (add-to-rolls game-state dice-value)))

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
    (let [initial-game-state {:current-player-rolls '(4 6 5)}
          dice-value 1]
      (is (empty? (:current-player-rolls (roll initial-game-state 1)))))))