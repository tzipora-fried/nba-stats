package com.example.nbastats.services.unit;

import com.example.nbastats.services.RedisUpdater;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.Map;

import static org.mockito.Mockito.*;

class RedisUpdaterTest {

    @Mock
    private JdbcTemplate jdbcTemplate;

    @Mock
    private RedisTemplate<String, Object> redisTemplate;

    @Mock
    private HashOperations<String, String, Object> hashOperations;

    @InjectMocks
    private RedisUpdater redisUpdater;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        when(redisTemplate.<String, Object>opsForHash()).thenReturn(hashOperations); // Mock `opsForHash()`
    }

    @Test
    void testUpdatePlayerStats() {
        Map<String, Object> playerStats = Map.of(
                "avg_points", 20.5,
                "avg_rebounds", 8.2,
                "avg_assists", 5.3,
                "avg_steals", 1.5,
                "avg_blocks", 0.7,
                "avg_fouls", 2.4,
                "avg_turnovers", 3.1
        );
        when(jdbcTemplate.queryForMap(anyString(), anyInt(), anyInt())).thenReturn(playerStats);
        redisUpdater.savePlayerStats(123, 2023);
        verify(hashOperations, times(1)).put("player:123:season:2023", "avg_points", 20.5);
        verify(hashOperations, times(1)).put("player:123:season:2023", "avg_rebounds", 8.2);
        verify(hashOperations, times(1)).put("player:123:season:2023", "avg_assists", 5.3);
    }

    @Test
    void testUpdateTeamStats() {
        Map<String, Object> teamStats = Map.of(
                "avg_points", 100.5,
                "avg_rebounds", 40.2,
                "avg_assists", 25.3,
                "avg_steals", 8.5,
                "avg_blocks", 4.1,
                "avg_fouls", 18.4,
                "avg_turnovers", 13.7
        );
        when(jdbcTemplate.queryForMap(anyString(), anyInt(), anyInt())).thenReturn(teamStats);
        redisUpdater.saveTeamStats(456, 2023);
        verify(redisTemplate.opsForHash(), times(1)).put("team:456:season:2023", "avg_points", 100.5);
        verify(redisTemplate.opsForHash(), times(1)).put("team:456:season:2023", "avg_rebounds", 40.2);
        verify(redisTemplate.opsForHash(), times(1)).put("team:456:season:2023", "avg_assists", 25.3);
    }
}