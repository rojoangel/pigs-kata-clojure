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

(defn- show-game-state [game-state]
  (let [scores (str/join ", " (:scores game-state))
        player-turn (str "player " (:player-turn game-state))
        turn (str/join ", " (:current-player-rolls game-state))]
    (println "scores: [" scores "] " player-turn " turn: [" turn "]")))

(defn- read-command []
  (do
    (print "roll or hold? > ")
    (flush)
    (let [[command-str & _] (str/split (read-line) #" ")]
      (keyword command-str))))

(defn- show-roll [dice-value]
  (print "you got a" dice-value)
  (if (= 1 dice-value)
    (println ". You lose your turn.")
    (println ". You can keep rolling.")))

(defn- dispatch [game-state command]
  (case command

    :hold
    (let [new-state (pigs/hold game-state)]
      (do
        (show-game-state new-state)
        (dispatch new-state (read-command))))

    :roll
    (let [dice-value (inc (rand-int 6))
          new-state (pigs/roll game-state dice-value)]
      (do
        (show-roll dice-value)
        (show-game-state new-state)
        (dispatch new-state (read-command))))))

(defn -main [& args]
  (do
    (salute)
    (let [initial-game-state (pigs/new-game (read-number-of-players))]
      (do
        (show-game-state initial-game-state)
        (dispatch initial-game-state (read-command))))))
