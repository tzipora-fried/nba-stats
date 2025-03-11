package com.example.nbastats.controllers;

import com.example.nbastats.services.StatsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/stats")
public class StatsController {
    private static final Logger logger = LoggerFactory.getLogger(StatsController.class);

    @Autowired
    private StatsService statsService;

    @GetMapping("/player/{playerId}/season/{seasonYear}")
    public Map<Object, Object> getPlayerStats(@PathVariable String playerId, @PathVariable String seasonYear) {
        try {
            Map<Object, Object> stats = statsService.getPlayerStats(playerId, seasonYear);
            if (stats == null) {
                logger.error("No stats found for player ID: {} in season: {}", playerId, seasonYear);
                return null;
            }
            return stats;
        } catch (Exception e) {
            logger.error("Error getting player stats: {}", e.getMessage());
            throw e;
        }
    }

    @GetMapping("/team/{teamName}/season/{seasonYear}")
    public Map<Object, Object> getTeamStats(@PathVariable String teamName, @PathVariable String seasonYear) {
        try {
            Map<Object, Object> stats = statsService.getTeamStats(teamName, seasonYear);
            if (stats == null) {
                logger.error("No stats found for team Name: {} in season: {}", teamName, seasonYear);
                return null;
            }
            return stats;
        } catch (Exception e) {
            logger.error("Error getting team stats: {}", e.getMessage());
            throw e;
        }
    }

}
