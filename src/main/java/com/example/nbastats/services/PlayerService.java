package com.example.nbastats.services;

import com.example.nbastats.dto.GameStats;
import com.example.nbastats.dto.PlayerRequestDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class PlayerService {

    @Autowired
    private JdbcTemplate jdbcTemplate;


    public String createPlayer(PlayerRequestDTO playerRequestDTO) {
        if (checkIfPlayerExists(playerRequestDTO.getId_number())) {
            return "Player with this ID number already exists!";
        }

        int teamId = getTeamIdByName(playerRequestDTO.getTeam_name());
        if (teamId == -1) {
            // אם הקבוצה לא קיימת, ניצור אותה
            teamId = saveTeam(playerRequestDTO.getTeam_name());
        }

        savePlayer(playerRequestDTO, teamId);

        saveGameStats(playerRequestDTO, teamId);

        return "Player and game stats inserted successfully!";
    }

    private boolean checkIfPlayerExists(String idNumber) {
        String sql = "SELECT COUNT(*) FROM players WHERE id_number = ?";
        int count = jdbcTemplate.queryForObject(sql, new Object[]{idNumber}, Integer.class);
        return count > 0;
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

    private void savePlayer(PlayerRequestDTO playerRequestDTO, int teamId) {
        String sql = "INSERT INTO players (name, id_number, team_id) VALUES (?, ?, ?)";
        jdbcTemplate.update(sql, playerRequestDTO.getName(), playerRequestDTO.getId_number(), teamId);
    }

    private void saveGameStats(PlayerRequestDTO playerRequestDTO, int teamId) {
        String sql = "INSERT INTO game_stats (game_date, player_id, points, rebounds, assists, steals, blocks, fouls, turnovers, minutes_played)\n" +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        Long playerId = getPlayerIdByNumber(playerRequestDTO.getId_number());

        GameStats gameStats = playerRequestDTO.getGame_stats();

        jdbcTemplate.update(
                sql,
                gameStats.getGame_date(),
                playerId,
                gameStats.getPoints(),
                gameStats.getRebounds(),
                gameStats.getAssists(),
                gameStats.getSteals(),
                gameStats.getBlocks(),
                gameStats.getFouls(),
                gameStats.getTurnovers(),
                gameStats.getMinutes_played()
        );

    }

    private Long getPlayerIdByNumber(String idNumber) {
        String sql = "SELECT id FROM players WHERE id_number = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{idNumber}, Long.class);
    }
}
