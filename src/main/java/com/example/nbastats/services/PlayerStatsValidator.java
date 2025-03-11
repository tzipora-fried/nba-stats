package com.example.nbastats.services;

import com.example.nbastats.dto.GameStats;
import org.springframework.stereotype.Service;

@Service
public class PlayerStatsValidator {

    public void validateStats(GameStats gameStats) {
        if (gameStats.getPoints() < 0) {
            throw new IllegalArgumentException("Points cannot be negative");
        }
        if (gameStats.getMinutesPlayed() < 0 || gameStats.getMinutesPlayed() > 48) {
            throw new IllegalArgumentException("Minutes played must be between 0 and 48");
        }
        if (gameStats.getFouls() < 0 || gameStats.getFouls() > 6) {
            throw new IllegalArgumentException("Fouls cannot be greater than 6");
        }

    }
}
