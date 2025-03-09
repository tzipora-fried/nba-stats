package com.example.nbastats.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class RedisUpdater {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Scheduled(fixedRate = 60000) // רץ כל דקה
    public void updateRedisCache() {
        List<Map<String, Object>> changes = jdbcTemplate.queryForList("SELECT * FROM change_log");

        for (Map<String, Object> change : changes) {
            String entityType = (String) change.get("entity_type");
            int entityId = (Integer) change.get("entity_id");

            if ("player".equals(entityType)) {
                updatePlayerStats(entityId);
            } else if ("team".equals(entityType)) {
                updateTeamStats(entityId);
            }
        }

        // ננקה את הטבלה אחרי שסיימנו לעדכן
        jdbcTemplate.update("DELETE FROM change_log");
    }

    private void updatePlayerStats(int playerId) {
        String sql = "SELECT player_id, AVG(points) as avg_points, AVG(rebounds) as avg_rebounds FROM game_stats WHERE player_id = ? GROUP BY player_id";
        Map<String, Object> stats = jdbcTemplate.queryForMap(sql, playerId);
        redisTemplate.opsForHash().put("player:stats", "player:" + playerId, stats);
    }

    private void updateTeamStats(int teamId) {
        String sql = "SELECT team_id, AVG(points) as avg_points, AVG(rebounds) as avg_rebounds FROM game_stats INNER JOIN players ON game_stats.player_id = players.id WHERE team_id = ? GROUP BY team_id";
        Map<String, Object> stats = jdbcTemplate.queryForMap(sql, teamId);
        redisTemplate.opsForHash().put("team:stats", "team:" + teamId, stats);
    }
}
