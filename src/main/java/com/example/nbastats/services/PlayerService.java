package com.example.nbastats.services;

import com.example.nbastats.dto.GameStats;
import com.example.nbastats.dto.PlayerRequestDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class PlayerService {

    private static final Logger logger = LoggerFactory.getLogger(PlayerService.class);

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private PlayerStatsValidator playerStatsValidator;

    public void createPlayer(PlayerRequestDTO playerRequestDTO) {
        playerStatsValidator.validateStats(playerRequestDTO.getGameStats());
        Integer playerId = getPlayerIdByIdNumber(playerRequestDTO.getIdNumber());
        if (playerId == -1) {
            int teamId = getTeamIdByName(playerRequestDTO.getTeamName());
            if (teamId == -1) {
                teamId = saveTeam(playerRequestDTO.getTeamName());
                logger.debug("Team {} created", playerRequestDTO.getTeamName());
            }
            playerId = savePlayer(playerRequestDTO, teamId);
            logger.debug("Player {} created", playerRequestDTO.getName());
        }
        saveGameStats(playerRequestDTO, playerId);
        logger.debug("Game stats for player {} created", playerRequestDTO.getName());
    }

    public Integer getPlayerIdByIdNumber(String idNumber) {
        String sql = "SELECT id FROM players WHERE id_number = ?";
        try {
            return jdbcTemplate.queryForObject(sql, new Object[]{idNumber}, Integer.class);
        } catch (EmptyResultDataAccessException e) {
            return -1;
        }
    }

    public Integer getTeamIdByName(String teamName) {
        String sql = "SELECT id FROM teams WHERE name = ?";
        try {
            return jdbcTemplate.queryForObject(sql, Integer.class, teamName);
        } catch (EmptyResultDataAccessException e) {
            return -1;
        }
    }

    private int saveTeam(String teamName) {
        String sql = "INSERT INTO teams (name) VALUES (?)";
        jdbcTemplate.update(sql, teamName);
        String selectSql = "SELECT id FROM teams WHERE name = ?";
        return jdbcTemplate.queryForObject(selectSql, new Object[]{teamName}, Integer.class);
    }

    private Integer savePlayer(PlayerRequestDTO playerRequestDTO, int teamId) {
        String sql = "INSERT INTO players (name, id_number, team_id) VALUES (?, ?, ?)";
        jdbcTemplate.update(sql, playerRequestDTO.getName(), playerRequestDTO.getIdNumber(), teamId);
        String selectSql = "SELECT id FROM players WHERE id_number = ?";
        return jdbcTemplate.queryForObject(selectSql, new Object[]{playerRequestDTO.getIdNumber()}, Integer.class);
    }

    private void saveGameStats(PlayerRequestDTO playerRequestDTO, Integer playerId) {
        String sql = "INSERT INTO game_stats (game_date, player_id, points, rebounds, assists, steals, blocks, fouls, turnovers, minutes_played)\n" +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        GameStats gameStats = playerRequestDTO.getGameStats();
        jdbcTemplate.update(
                sql,
                gameStats.getGameDate(),
                playerId,
                gameStats.getPoints(),
                gameStats.getRebounds(),
                gameStats.getAssists(),
                gameStats.getSteals(),
                gameStats.getBlocks(),
                gameStats.getFouls(),
                gameStats.getTurnovers(),
                gameStats.getMinutesPlayed()
        );

    }
}
