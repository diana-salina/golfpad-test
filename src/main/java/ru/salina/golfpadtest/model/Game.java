package ru.salina.golfpadtest.model;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Game {
    private List<GolfPlayer> players;
    private List<Integer> skinsWon;
    private String winner;
    private List<Integer> playerTotalSkins;
    private int holesAmount;

    public Game() {
        this.players = new ArrayList<>();
        this.skinsWon = new ArrayList<>();
        this.playerTotalSkins = new ArrayList<>();
    }

    public Game(List<GolfPlayer> players, int holesAmount) {
        this.players = players;
        this.holesAmount = holesAmount;
        this.skinsWon = new ArrayList<>();
        this.playerTotalSkins = new ArrayList<>();
        for (int i = 0; i < players.size(); i++) {
            this.playerTotalSkins.add(0);
        }
        calculateSkins();
    }
    public void calculateSkins() {
        skinsWon.clear();
        playerTotalSkins.replaceAll(ignored -> 0);

        int accumulatedSkins = 1;
        for (int hole = 0; hole < holesAmount; hole++) {
            List<Integer> holeScores = new ArrayList<>();
            for (GolfPlayer player : players) {
                List<Integer> playerScores = player.getScores();
                if (playerScores != null && hole < playerScores.size()) {
                    holeScores.add(playerScores.get(hole));
                } else {
                    holeScores.add(0);
                }
            }
            List<Integer> validScores = holeScores.stream()
                .filter(score -> score > 0)
                .collect(Collectors.toList());
            
            if (validScores.isEmpty()) {
                skinsWon.add(0);
                accumulatedSkins++;
                continue;
            }
            
            int bestScore = validScores.stream().min(Integer::compareTo).orElse(Integer.MAX_VALUE);

            List<Integer> winners = new ArrayList<>();
            for (int i = 0; i < holeScores.size(); i++) {
                if (holeScores.get(i) == bestScore && holeScores.get(i) > 0) {
                    winners.add(i);
                }
            }

            if (winners.size() == 1) {
                int winnerIndex = winners.get(0);
                skinsWon.add(winnerIndex + 1);
                playerTotalSkins.set(winnerIndex, playerTotalSkins.get(winnerIndex) + accumulatedSkins);
                accumulatedSkins = 1;
            } else {
                skinsWon.add(0);
                accumulatedSkins++;
            }
        }

        determineWinner();
    }

    private void determineWinner() {
        int maxSkins = playerTotalSkins.stream().max(Integer::compareTo).orElse(0);
        List<Integer> winners = new ArrayList<>();
        
        for (int i = 0; i < playerTotalSkins.size(); i++) {
            if (playerTotalSkins.get(i) == maxSkins) {
                winners.add(i);
            }
        }

        if (winners.size() == 1) {
            winner = "Winner: " + players.get(winners.get(0)).getName();
        } else {
            winner = "Complete Tie";
        }
    }

    public List<GolfPlayer> getPlayers() {
        return players;
    }

    public List<Integer> getSkinsWon() {
        return skinsWon;
    }

    public String getWinner() {
        return winner;
    }

    public List<Integer> getPlayerTotalSkins() {
        return playerTotalSkins;
    }
}