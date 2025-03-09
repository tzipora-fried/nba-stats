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

    // יצירת שחקן חדש
    public String createPlayer(PlayerRequestDTO playerRequestDTO) {
        // בדיקת קיום של השחקן לפי תעודת זהות
        if (checkIfPlayerExists(playerRequestDTO.getId_number())) {
            return "Player with this ID number already exists!";
        }

        // בדיקת קיום של קבוצה
        int teamId = getTeamIdByName(playerRequestDTO.getTeam_name());
        if (teamId == -1) {
            // אם הקבוצה לא קיימת, ניצור אותה
            teamId = saveTeam(playerRequestDTO.getTeam_name());
        }

        // הוספת שחקן
        savePlayer(playerRequestDTO, teamId);

        // יצירת סטטיסטיקות המשחק
        saveGameStats(playerRequestDTO, teamId);

        return "Player and game stats inserted successfully!";
    }

    // Method to check if a player already exists by ID number
    private boolean checkIfPlayerExists(String idNumber) {
        String sql = "SELECT COUNT(*) FROM players WHERE id_number = ?";
        int count = jdbcTemplate.queryForObject(sql, new Object[]{idNumber}, Integer.class);
        return count > 0;
    }

    // Method to get team ID by name
    public Integer getTeamIdByName(String teamName) {
        String sql = "SELECT id FROM teams WHERE name = ?";
        try {
            return jdbcTemplate.queryForObject(sql, Integer.class, teamName);
        } catch (EmptyResultDataAccessException e) {
            // במקרה שאין קבוצה כזו, החזר null או ערך אחר
            return -1;
        }
    }

    // Method to save a new team to the database
    private int saveTeam(String teamName) {
        String sql = "INSERT INTO teams (name) VALUES (?)";
        jdbcTemplate.update(sql, teamName);

        // מחזירים את ה-ID של הקבוצה שנשמרה
        String selectSql = "SELECT id FROM teams WHERE name = ?";
        return jdbcTemplate.queryForObject(selectSql, new Object[]{teamName}, Integer.class);
    }

    // Method to save player to the database
    private void savePlayer(PlayerRequestDTO playerRequestDTO, int teamId) {
        String sql = "INSERT INTO players (name, id_number, team_id) VALUES (?, ?, ?)";
        jdbcTemplate.update(sql, playerRequestDTO.getName(), playerRequestDTO.getId_number(), teamId);
    }

    // Method to save game stats to the database
    private void saveGameStats(PlayerRequestDTO playerRequestDTO, int teamId) {
        String sql = "INSERT INTO game_stats (game_date, player_id, points, rebounds, assists, steals, blocks, fouls, turnovers, minutes_played)\n" +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        // ניתן להניח ש-id של השחקן כבר נוצר עם השחקן ולכן השחקן יוכל להשתמש ב-Id הזה
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

    // Method to get player ID by ID number
    private Long getPlayerIdByNumber(String idNumber) {
        String sql = "SELECT id FROM players WHERE id_number = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{idNumber}, Long.class);
    }
}
