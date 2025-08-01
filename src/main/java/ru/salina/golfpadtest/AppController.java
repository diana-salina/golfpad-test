package ru.salina.golfpadtest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.salina.golfpadtest.model.Game;
import ru.salina.golfpadtest.service.GameService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
public class AppController {

    @Autowired
    private GameService gameService;
    
    private static final int PLAYERS_COUNT = 2;

    @GetMapping("/")
    public String showRegistrationPage(Model model) {
        List<String> playerNames = createEmptyPlayerNames();
        model.addAttribute("playerNames", playerNames);
        addCommonAttributes(model);
        return "registration";
    }

    @PostMapping("/game")
    public String startGame(@RequestParam Map<String, String> allParams, Model model) {
        try {
            List<String> playerNames = new ArrayList<>();
            for (int i = 0; i < PLAYERS_COUNT; i++) {
                String name = allParams.get("playerName" + i);
                if (name == null || name.trim().isEmpty()) {
                    throw new IllegalArgumentException("All player names are required");
                }
                playerNames.add(name.trim());
            }

            List<List<Integer>> playerScores = createEmptyPlayerScores();
            
            model.addAttribute("playerNames", playerNames);
            model.addAttribute("playerScores", playerScores);
            addCommonAttributes(model);
            return "main";
            
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            
            List<String> playerNames = new ArrayList<>();
            for (int i = 0; i < PLAYERS_COUNT; i++) {
                playerNames.add(allParams.getOrDefault("playerName" + i, ""));
            }
            
            model.addAttribute("playerNames", playerNames);
            addCommonAttributes(model);
            return "registration";
        }
    }

    @PostMapping("/calculate")
    public String calculate(@RequestParam Map<String, String> allParams, Model model) {
        try {
            List<String> playerNames = extractPlayerNames(allParams);
            List<List<Integer>> playerScores = parsePlayerScores(allParams);

            Game game = gameService.calculateGame(playerNames, playerScores);

            model.addAttribute("playerNames", playerNames);
            model.addAttribute("playerScores", playerScores);
            addCommonAttributes(model);
            model.addAttribute("game", game);
            
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());

            List<String> playerNames = extractPlayerNames(allParams);
            List<List<Integer>> playerScores = parsePlayerScoresWithZeros(allParams);
            
            model.addAttribute("playerNames", playerNames);
            model.addAttribute("playerScores", playerScores);
            addCommonAttributes(model);
        }
        
        return "main";
    }

    // Helper methods to eliminate code duplication
    private List<String> createEmptyPlayerNames() {
        List<String> playerNames = new ArrayList<>();
        for (int i = 0; i < PLAYERS_COUNT; i++) {
            playerNames.add("");
        }
        return playerNames;
    }

    private List<String> extractPlayerNames(Map<String, String> allParams) {
        List<String> playerNames = new ArrayList<>();
        for (int i = 0; i < PLAYERS_COUNT; i++) {
            String name = allParams.get("playerName" + i);
            playerNames.add(name != null && !name.trim().isEmpty() ? name.trim() : "Player " + (i + 1));
        }
        return playerNames;
    }

    private List<List<Integer>> createEmptyPlayerScores() {
        int holesCount = gameService.getHolesAmount();
        List<List<Integer>> playerScores = new ArrayList<>();
        for (int i = 0; i < PLAYERS_COUNT; i++) {
            List<Integer> scores = new ArrayList<>();
            for (int j = 0; j < holesCount; j++) {
                scores.add(0);
            }
            playerScores.add(scores);
        }
        return playerScores;
    }

    private List<List<Integer>> parsePlayerScores(Map<String, String> allParams) {
        int holesCount = gameService.getHolesAmount();
        List<List<Integer>> playerScores = new ArrayList<>();
        for (int player = 0; player < PLAYERS_COUNT; player++) {
            List<Integer> playerHoleScores = new ArrayList<>();
            for (int hole = 1; hole <= holesCount; hole++) {
                String scoreParam = allParams.get("player" + player + "hole" + hole);
                int score = 0;
                if (scoreParam != null && !scoreParam.trim().isEmpty()) {
                    try {
                        score = Integer.parseInt(scoreParam.trim());
                        if (score <= 0) {
                            throw new IllegalArgumentException("Please enter all strokes. All scores must be greater than 0.");
                        }
                    } catch (NumberFormatException e) {
                        throw new IllegalArgumentException("Please enter all strokes. Invalid score format.");
                    }
                } else {
                    throw new IllegalArgumentException("Please enter all strokes. All holes must have scores.");
                }
                playerHoleScores.add(score);
            }
            playerScores.add(playerHoleScores);
        }
        return playerScores;
    }

    private List<List<Integer>> parsePlayerScoresWithZeros(Map<String, String> allParams) {
        int holesCount = gameService.getHolesAmount();
        List<List<Integer>> playerScores = new ArrayList<>();
        for (int player = 0; player < PLAYERS_COUNT; player++) {
            List<Integer> playerHoleScores = new ArrayList<>();
            for (int hole = 1; hole <= holesCount; hole++) {
                String scoreParam = allParams.get("player" + player + "hole" + hole);
                int score = 0;
                if (scoreParam != null && !scoreParam.trim().isEmpty()) {
                    try {
                        score = Integer.parseInt(scoreParam.trim());
                        if (score < 0) score = 0;
                    } catch (NumberFormatException e) {
                        score = 0;
                    }
                }
                playerHoleScores.add(score);
            }
            playerScores.add(playerHoleScores);
        }
        return playerScores;
    }

    private void addCommonAttributes(Model model) {
        int holesCount = gameService.getHolesAmount();
        model.addAttribute("playersAmount", PLAYERS_COUNT);
        model.addAttribute("holesAmount", holesCount);
    }
}
