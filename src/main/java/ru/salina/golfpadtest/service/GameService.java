package ru.salina.golfpadtest.service;

import org.springframework.stereotype.Service;
import ru.salina.golfpadtest.model.GolfPlayer;
import ru.salina.golfpadtest.model.Game;

import java.util.List;

@Service
public class GameService {
    public Game calculateGame(String player1Name, List<Integer> player1Scores,
                              String player2Name, List<Integer> player2Scores) {
        
        if (player1Scores.size() != 18 || player2Scores.size() != 18) {
            throw new IllegalArgumentException("Each player must have exactly 18 scores");
        }

        GolfPlayer player1 = new GolfPlayer(player1Name, player1Scores);
        GolfPlayer player2 = new GolfPlayer(player2Name, player2Scores);

        return new Game(player1, player2);
    }
} 