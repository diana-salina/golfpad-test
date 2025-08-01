package ru.salina.golfpadtest.model;

import java.util.ArrayList;
import java.util.List;

public class Game {
    private GolfPlayer player1;
    private GolfPlayer player2;
    private List<Integer> skinsWon;
    private String winner;

    public Game() {
        this.skinsWon = new ArrayList<>();
    }

    public Game(GolfPlayer player1, GolfPlayer player2) {
        this.player1 = player1;
        this.player2 = player2;
        this.skinsWon = new ArrayList<>();
        calculateSkins();
    }

    public void calculateSkins() {
        skinsWon.clear();
        List<Integer> scores1 = player1.getScores();
        List<Integer> scores2 = player2.getScores();

        int accumulatedSkins = 1;

        for (int i = 0; i < 18; i++) {
            int score1 = scores1.get(i);
            int score2 = scores2.get(i);

            if (score1 < score2) {
                // Player 1 wins - gets all accumulated skins
                for (int j = 0; j < accumulatedSkins; j++) {
                    skinsWon.add(1);
                }
                accumulatedSkins = 1; // Reset for next hole
            } else if (score2 < score1) {
                // Player 2 wins - gets all accumulated skins
                for (int j = 0; j < accumulatedSkins; j++) {
                    skinsWon.add(2);
                }
                accumulatedSkins = 1; // Reset for next hole
            } else {
                // Tie - skin carries over to next hole
                skinsWon.add(0);
                accumulatedSkins++; // Add another skin for next hole
            }
        }

        determineWinner();
    }

    private void determineWinner() {
        int player1Skins = (int) skinsWon.stream().filter(skin -> skin == 1).count();
        int player2Skins = (int) skinsWon.stream().filter(skin -> skin == 2).count();

        if (player1Skins > player2Skins) {
            winner = player1.getName();
        } else if (player2Skins > player1Skins) {
            winner = player2.getName();
        } else {
            winner = "Ничья";
        }
    }

    public GolfPlayer getPlayer1() {
        return player1;
    }

    public void setPlayer1(GolfPlayer player1) {
        this.player1 = player1;
    }

    public GolfPlayer getPlayer2() {
        return player2;
    }

    public void setPlayer2(GolfPlayer player2) {
        this.player2 = player2;
    }

    public List<Integer> getSkinsWon() {
        return skinsWon;
    }

    public void setSkinsWon(List<Integer> skinsWon) {
        this.skinsWon = skinsWon;
    }

    public String getWinner() {
        return winner;
    }

    public void setWinner(String winner) {
        this.winner = winner;
    }

    public int getPlayer1Skins() {
        return (int) skinsWon.stream().filter(skin -> skin == 1).count();
    }

    public int getPlayer2Skins() {
        return (int) skinsWon.stream().filter(skin -> skin == 2).count();
    }
} 