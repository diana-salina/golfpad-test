package ru.salina.golfpadtest.model;

import java.util.List;

public class GolfPlayer {
    private final String name;
    private final List<Integer> scores;

    public GolfPlayer(String name, List<Integer> scores) {
        this.name = name;
        this.scores = scores;
    }

    public String getName() {
        return name;
    }

    public List<Integer> getScores() {
        return scores;
    }
} 