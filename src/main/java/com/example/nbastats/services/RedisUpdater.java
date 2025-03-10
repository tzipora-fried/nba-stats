package com.example.nbastats.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class RedisUpdater {

    private static final Logger logger = LoggerFactory.getLogger(RedisUpdater.class);

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Scheduled(fixedRate = 60000)
    public void updateRedisCache() {
        List<Map<String, Object>> changes = jdbcTemplate.queryForList("SELECT * FROM change_log");

        for (Map<String, Object> change : changes) {
            String entityType = (String) change.get("entity_type");
            int entityId = (Integer) change.get("entity_id");
            String seasonYear = (String) change.get("season_year");
            if ("player".equals(entityType)) {
                updatePlayerStats(entityId, seasonYear);
                logger.debug("Updated stats for player {}", entityId);
            } else if ("team".equals(entityType)) {
                updateTeamStats(entityId, seasonYear);
                logger.debug("Updated stats for team {}", entityId);
            }
        }

        jdbcTemplate.update("DELETE FROM change_log");
        logger.debug("Cleared change log");
    }

    private void updatePlayerStats(int playerId, String seasonYear) {
        String sql = "SELECT player_id, AVG(points) as avg_points, AVG(rebounds) as avg_rebounds, " +
                "YEAR(game_date) as season_year " +
                "FROM game_stats WHERE player_id = ? AND YEAR(game_date) = ? GROUP BY player_id, YEAR(game_date)";
        Map<String, Object> stats = jdbcTemplate.queryForMap(sql, playerId, seasonYear);

        // Save player stats in Redis, including season
        String redisKey = "player:" + playerId + ":season:" + seasonYear;
        redisTemplate.opsForHash().put(redisKey, "avg_points", stats.get("avg_points"));
        redisTemplate.opsForHash().put(redisKey, "avg_rebounds", stats.get("avg_rebounds"));
    }

    private void updateTeamStats(int teamId, String seasonYear) {
        String sql = "SELECT team_id, AVG(points) as avg_points, AVG(rebounds) as avg_rebounds, " +
                "YEAR(game_date) as season_year " +
                "FROM game_stats INNER JOIN players ON game_stats.player_id = players.id " +
                "WHERE team_id = ? AND YEAR(game_date) = ? GROUP BY team_id, YEAR(game_date)";
        Map<String, Object> stats = jdbcTemplate.queryForMap(sql, teamId, seasonYear);

        // Save team stats in Redis, including season
        String redisKey = "team:" + teamId + ":season:" + seasonYear;
        redisTemplate.opsForHash().put(redisKey, "avg_points", stats.get("avg_points"));
        redisTemplate.opsForHash().put(redisKey, "avg_rebounds", stats.get("avg_rebounds"));
    }
}
