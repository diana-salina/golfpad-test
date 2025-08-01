package ru.salina.golfpadtest.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import ru.salina.golfpadtest.model.GolfPlayer;
import ru.salina.golfpadtest.model.Game;

import java.util.ArrayList;
import java.util.List;

@Service
@PropertySource("classpath:config.properties")
public class GameService {
    @Value("${holesAmount}")
    private int holesAmount;

    public Game calculateGame(List<String> playerNames, List<List<Integer>> playerScores) {
        for (List<Integer> scores : playerScores) {
            if (scores.size() != holesAmount) {
                throw new IllegalArgumentException("Each player must have exactly " + holesAmount + " scores");
            }
        }

        List<GolfPlayer> players = new ArrayList<>();
        for (int i = 0; i < playerNames.size(); i++) {
            players.add(new GolfPlayer(playerNames.get(i), playerScores.get(i)));
        }

        return new Game(players, holesAmount);
    }

    public int getHolesAmount() {
        return holesAmount;
    }
}