package com.example.nbastats.controllers;

import com.example.nbastats.services.StatsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/stats")
public class StatsController {

    @Autowired
    private StatsService statsService;

    @GetMapping("/player/{playerId}")
    public Map<String, Object> getPlayerStats(@PathVariable int playerId) {
        Map<String, Object> stats = statsService.getPlayerStats(playerId);

        if (stats == null) {
            throw new RuntimeException("❌ No stats found for player ID: " + playerId);
        }

        return stats;
    }

    @GetMapping("/team/{teamId}")
    public Map<String, Object> getTeamStats(@PathVariable int teamId) {
        Map<String, Object> stats = statsService.getTeamStats(teamId);

        if (stats == null) {
            throw new RuntimeException("❌ No stats found for team ID: " + teamId);
        }

        return stats;
    }
}
