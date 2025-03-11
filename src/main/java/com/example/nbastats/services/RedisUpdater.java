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
            int playerId = (Integer) change.get("player_id");
            int seasonYear = (Integer) change.get("season_year");
            updatePlayerStats(playerId, seasonYear);
            Integer teamId = getTeamId(playerId);
            updateTeamStats(teamId, seasonYear);
        }

        jdbcTemplate.update("DELETE FROM change_log");
        logger.debug("Cleared change log");
    }

    private void updatePlayerStats(int playerId, int seasonYear) {
        String sql = "SELECT player_id, AVG(points) as avg_points, AVG(rebounds) as avg_rebounds, AVG(assists) as avg_assists, AVG(steals) as avg_steals , AVG(blocks) as avg_blocks, AVG(fouls) as avg_fouls, AVG(turnovers) as avg_turnovers, " +
                "YEAR(game_date) as season_year " +
                "FROM game_stats WHERE player_id = ? AND YEAR(game_date) = ? GROUP BY player_id, YEAR(game_date)";
        Map<String, Object> stats = jdbcTemplate.queryForMap(sql, playerId, seasonYear);
        String redisKey = "player:" + playerId + ":season:" + seasonYear;
        setAvgStats(stats, redisKey);
    }

    private void updateTeamStats(int teamId, Integer seasonYear) {
        String sql = "SELECT team_id, AVG(points) as avg_points, AVG(rebounds) as avg_rebounds, AVG(assists) as avg_assists, AVG(steals) as avg_steals , AVG(blocks) as avg_blocks, AVG(fouls) as avg_fouls, AVG(turnovers) as avg_turnovers, " +
                "YEAR(game_date) as season_year " +
                "FROM game_stats INNER JOIN players ON game_stats.player_id = players.id " +
                "WHERE team_id = ? AND YEAR(game_date) = ? GROUP BY team_id, YEAR(game_date)";
        Map<String, Object> stats = jdbcTemplate.queryForMap(sql, teamId, seasonYear);
        String redisKey = "team:" + teamId + ":season:" + seasonYear;
        setAvgStats(stats, redisKey);
    }

    private void setAvgStats(Map<String, Object> stats, String redisKey) {
        redisTemplate.opsForHash().put(redisKey, "avg_points", stats.get("avg_points"));
        redisTemplate.opsForHash().put(redisKey, "avg_rebounds", stats.get("avg_rebounds"));
        redisTemplate.opsForHash().put(redisKey, "avg_assists", stats.get("avg_assists"));
        redisTemplate.opsForHash().put(redisKey, "avg_steals", stats.get("avg_steals"));
        redisTemplate.opsForHash().put(redisKey, "avg_blocks", stats.get("avg_blocks"));
        redisTemplate.opsForHash().put(redisKey, "avg_fouls", stats.get("avg_fouls"));
        redisTemplate.opsForHash().put(redisKey, "avg_turnovers", stats.get("avg_turnovers"));
    }

    private Integer getTeamId(Integer playerId) {
        String sql = "SELECT team_id FROM players WHERE id = ?";
        int team_id = jdbcTemplate.queryForObject(sql, new Object[]{playerId}, Integer.class);
        return team_id;
    }
}
