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
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Controller
public class AppController {

    @Autowired
    private GameService gameService;

    @GetMapping("/")
    public String showRegistrationPage(Model model) {
        model.addAttribute("player1Name", "");
        model.addAttribute("player2Name", "");
        return "registration";
    }

    @PostMapping("/game")
    public String startGame(@RequestParam("player1Name") String player1Name,
                           @RequestParam("player2Name") String player2Name,
                           Model model) {
        if (player1Name == null || player1Name.trim().isEmpty() || 
            player2Name == null || player2Name.trim().isEmpty()) {
            model.addAttribute("error", "Both players' names are required");
            model.addAttribute("player1Name", player1Name);
            model.addAttribute("player2Name", player2Name);
            return "registration";
        }

        List<Integer> emptyScores1 = new ArrayList<>(Collections.nCopies(18, 0));
        List<Integer> emptyScores2 = new ArrayList<>(Collections.nCopies(18, 0));

        model.addAttribute("player1Scores", emptyScores1);
        model.addAttribute("player2Scores", emptyScores2);
        model.addAttribute("player1Name", player1Name);
        model.addAttribute("player2Name", player2Name);
        
        return "main";
    }

    @PostMapping("/calculate")
    public String calculate(@RequestParam("player1Name") String player1Name,
                            @RequestParam("player2Name") String player2Name,
                            @RequestParam("player1Scores") List<Integer> player1Scores,
                            @RequestParam("player2Scores") List<Integer> player2Scores,
                            Model model) {
        
        System.out.println("DEBUG: calculate - player1Name = " + player1Name);
        System.out.println("DEBUG: calculate - player2Name = " + player2Name);
        System.out.println("DEBUG: calculate - player1Scores size = " + (player1Scores != null ? player1Scores.size() : "null"));
        System.out.println("DEBUG: calculate - player2Scores size = " + (player2Scores != null ? player2Scores.size() : "null"));
        
        try {
            assert player1Scores != null;
            assert player2Scores != null;
            Game game = gameService.calculateGame(player1Name, player1Scores,
                                                               player2Name, player2Scores);

            int player1Total = safeSum(player1Scores);
            int player2Total = safeSum(player2Scores);

            String leader = null;
            if (player1Total < player2Total) {
                leader = player1Name;
            } else if (player2Total < player1Total) {
                leader = player2Name;
            }

            model.addAttribute("game", game);
            model.addAttribute("player1Name", player1Name);
            model.addAttribute("player2Name", player2Name);
            model.addAttribute("player1Scores", player1Scores);
            model.addAttribute("player2Scores", player2Scores);
            model.addAttribute("player1Total", player1Total);
            model.addAttribute("player2Total", player2Total);
            model.addAttribute("leader", leader);
            model.addAttribute("summaryVisible", true);

        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("player1Name", player1Name);
            model.addAttribute("player2Name", player2Name);
            model.addAttribute("player1Scores", player1Scores);
            model.addAttribute("player2Scores", player2Scores);

            model.addAttribute("player1Total", 0);
            model.addAttribute("player2Total", 0);
            model.addAttribute("leader", null);
            model.addAttribute("summaryVisible", true);
        }
        return "main";
    }

    private int safeSum(List<Integer> scores) {
        if (scores == null) return 0;
        return scores.stream().filter(Objects::nonNull).mapToInt(Integer::intValue).sum();
    }
}
