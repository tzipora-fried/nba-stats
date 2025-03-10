package com.example.nbastats.controllers;

import com.example.nbastats.services.StatsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/stats")
public class StatsController {
    private static final Logger logger = LoggerFactory.getLogger(StatsController.class);

    @Autowired
    private StatsService statsService;
    @GetMapping("/player/{playerId}/season/{seasonYear}")
    public Map<Object, Object> getPlayerStats(@PathVariable int playerId, @PathVariable String seasonYear) {
        Map<Object, Object> stats = statsService.getPlayerStats(playerId, seasonYear);
        if (stats == null) {
            logger.error("No stats found for player ID: {} in season: {}", playerId, seasonYear);
            return null;
        }
        return stats;
    }

    @GetMapping("/team/{teamId}/season/{seasonYear}")
    public Map<Object, Object> getTeamStats(@PathVariable int teamId, @PathVariable String seasonYear) {
        Map<Object, Object> stats = statsService.getTeamStats(teamId, seasonYear);
        if (stats == null) {
            logger.error("No stats found for team ID: {} in season: {}", teamId, seasonYear);
            return null;
        }
        return stats;
    }

}
