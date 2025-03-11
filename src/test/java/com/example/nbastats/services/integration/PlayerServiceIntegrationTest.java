//package com.example.nbastats.services.integration;
//
//import com.example.nbastats.dto.GameStats;
//import com.example.nbastats.services.PlayerService;
//import org.junit.jupiter.api.BeforeEach;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.core.env.Environment;
//import org.springframework.test.context.ActiveProfiles;
//import org.testcontainers.junit.jupiter.Testcontainers;
//import com.example.nbastats.dto.PlayerRequestDTO;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.jdbc.core.JdbcTemplate;
//
//import java.time.LocalDate;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//@SpringBootTest
//@Testcontainers
//@ActiveProfiles("test")
//public class PlayerServiceIntegrationTest {
//
//    @Autowired
//    private PlayerService playerService;
//
//    @Autowired
//    private JdbcTemplate jdbcTemplate;
//
//    @BeforeEach
//    void setUp() {
//        jdbcTemplate.update("DELETE FROM game_stats");
//        jdbcTemplate.update("DELETE FROM players");
//        jdbcTemplate.update("DELETE FROM teams");
//    }
//
//    @Test
//    void createPlayer_NewPlayerAndTeam_ShouldSaveAll() {
//        PlayerRequestDTO playerRequestDTO = new PlayerRequestDTO();
//        playerRequestDTO.setIdNumber("12345");
//        playerRequestDTO.setName("Alice Smith");
//        playerRequestDTO.setTeamName("Warriors");
//
//        GameStats gameStats = new GameStats();
//        gameStats.setGameDate(String.valueOf(LocalDate.now()));
//        gameStats.setPoints(25);
//        gameStats.setRebounds(8);
//        gameStats.setAssists(5);
//        gameStats.setSteals(2);
//        gameStats.setBlocks(1);
//        gameStats.setFouls(3);
//        gameStats.setTurnovers(2);
//        gameStats.setMinutesPlayed(35.0);
//
//        playerRequestDTO.setGameStats(gameStats);
//
//        playerService.createPlayer(playerRequestDTO);
//
//        Integer playerId = jdbcTemplate.queryForObject("SELECT id FROM players WHERE id_number = ?", Integer.class, "12345");
//        assertNotNull(playerId);
//        Integer teamId = jdbcTemplate.queryForObject("SELECT id FROM teams WHERE name = ?", Integer.class, "Warriors");
//        assertNotNull(teamId);
//
//        Integer gameStatsId = jdbcTemplate.queryForObject("SELECT count(*) FROM game_stats WHERE player_id = ?", Integer.class, playerId);
//        assertEquals(1, gameStatsId);
//    }
//
//    @Test
//    void createPlayer_ExistingTeam_ShouldSavePlayerAndGameStats() {
//        jdbcTemplate.update("INSERT INTO teams (name) VALUES (?)", "Spurs");
//
//        PlayerRequestDTO playerRequestDTO = new PlayerRequestDTO();
//        playerRequestDTO.setIdNumber("67890");
//        playerRequestDTO.setName("Bob Johnson");
//        playerRequestDTO.setTeamName("Spurs");
//
//        GameStats gameStats = new GameStats();
//        gameStats.setGameDate(String.valueOf(LocalDate.now()));
//        gameStats.setPoints(20);
//        gameStats.setRebounds(6);
//        gameStats.setAssists(4);
//        gameStats.setSteals(1);
//        gameStats.setBlocks(0);
//        gameStats.setFouls(2);
//        gameStats.setTurnovers(1);
//        gameStats.setMinutesPlayed(30.0);
//
//        playerRequestDTO.setGameStats(gameStats);
//
//        playerService.createPlayer(playerRequestDTO);
//
//        Integer playerId = jdbcTemplate.queryForObject("SELECT id FROM players WHERE id_number = ?", Integer.class, "67890");
//        assertNotNull(playerId);
//
//        Integer gameStatsId = jdbcTemplate.queryForObject("SELECT count(*) FROM game_stats WHERE player_id = ?", Integer.class, playerId);
//        assertEquals(1, gameStatsId);
//    }
//
//    @Test
//    void createPlayer_ExistingPlayer_ShouldSaveOnlyGameStats() {
//        jdbcTemplate.update("INSERT INTO teams (name) VALUES (?)", "Heat");
//        Integer teamId = playerService.getTeamIdByName("Heat"); // קבלת teamId נכון
//        jdbcTemplate.update("INSERT INTO players (name, id_number, team_id) VALUES (?, ?, ?)", "Charlie Brown", "11122", teamId); // שימוש ב teamId נכון
//        Integer playerId = playerService.getPlayerIdByIdNumber("11122");
//
//
//        PlayerRequestDTO playerRequestDTO = new PlayerRequestDTO();
//        playerRequestDTO.setIdNumber("11122");
//        playerRequestDTO.setName("Charlie Brown");
//        playerRequestDTO.setTeamName("Heat");
//
//        GameStats gameStats = new GameStats();
//        gameStats.setGameDate(String.valueOf(LocalDate.now()));
//        gameStats.setPoints(15);
//        gameStats.setRebounds(4);
//        gameStats.setAssists(3);
//        gameStats.setSteals(0);
//        gameStats.setBlocks(0);
//        gameStats.setFouls(1);
//        gameStats.setTurnovers(0);
//        gameStats.setMinutesPlayed(25.0);
//
//        playerRequestDTO.setGameStats(gameStats);
//
//        playerService.createPlayer(playerRequestDTO);
//
//        Integer gameStatsId = jdbcTemplate.queryForObject("SELECT count(*) FROM game_stats WHERE player_id = ?", Integer.class, playerId);
//        assertEquals(1, gameStatsId);
//    }
//}