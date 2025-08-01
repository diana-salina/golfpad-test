package ru.salina.golfpadtest.model;

import java.util.List;

public class GolfPlayer {
    private String name;
    private List<Integer> scores;

    public GolfPlayer() {
    }

    public GolfPlayer(String name, List<Integer> scores) {
        this.name = name;
        this.scores = scores;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Integer> getScores() {
        return scores;
    }

    public void setScores(List<Integer> scores) {
        this.scores = scores;
    }

    public int getTotalScore() {
        return scores.stream().mapToInt(Integer::intValue).sum();
    }
} 