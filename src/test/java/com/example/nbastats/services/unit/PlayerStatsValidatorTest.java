package com.example.nbastats.services.unit;

import com.example.nbastats.dto.GameStats;
import com.example.nbastats.services.PlayerStatsValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PlayerStatsValidatorTest {

    private PlayerStatsValidator playerStatsValidator;

    @BeforeEach
    void setUp() {
        playerStatsValidator = new PlayerStatsValidator();
    }

    @Test
    void validateStats_WithValidStats_ShouldNotThrowException() {
        GameStats validGameStats = new GameStats();
        validGameStats.setPoints(20);
        validGameStats.setMinutesPlayed(30.0);
        validGameStats.setFouls(3);
        assertDoesNotThrow(() -> playerStatsValidator.validateStats(validGameStats));
    }

    @Test
    void validateStats_WithNegativePoints_ShouldThrowIllegalArgumentException() {
        GameStats invalidGameStats = new GameStats();
        invalidGameStats.setPoints(-1);
        invalidGameStats.setMinutesPlayed(20.0);
        invalidGameStats.setFouls(3);
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> playerStatsValidator.validateStats(invalidGameStats));
        assertEquals("Points cannot be negative", exception.getMessage());
    }

    @Test
    void validateStats_WithInvalidMinutesPlayed_ShouldThrowIllegalArgumentException() {
        GameStats invalidGameStats = new GameStats();
        invalidGameStats.setPoints(10);
        invalidGameStats.setMinutesPlayed(60.0);
        invalidGameStats.setFouls(2);
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> playerStatsValidator.validateStats(invalidGameStats));
        assertEquals("Minutes played must be between 0 and 48", exception.getMessage());
    }

    @Test
    void validateStats_WithNegativeFouls_ShouldThrowIllegalArgumentException() {
        GameStats invalidGameStats = new GameStats();
        invalidGameStats.setPoints(10);
        invalidGameStats.setMinutesPlayed(20.0);
        invalidGameStats.setFouls(-1);
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> playerStatsValidator.validateStats(invalidGameStats));
        assertEquals("Fouls cannot be greater than 6", exception.getMessage());
    }

    @Test
    void validateStats_WithTooManyFouls_ShouldThrowIllegalArgumentException() {
        GameStats invalidGameStats = new GameStats();
        invalidGameStats.setPoints(10);
        invalidGameStats.setMinutesPlayed(20.0);
        invalidGameStats.setFouls(7);
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> playerStatsValidator.validateStats(invalidGameStats));
        assertEquals("Fouls cannot be greater than 6", exception.getMessage());
    }
}
