//package com.example.nbastats.services;
//
//import com.example.nbastats.dto.GameStats;
//import com.example.nbastats.dto.PlayerRequestDTO;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.*;
//import org.springframework.dao.EmptyResultDataAccessException;
//import org.springframework.jdbc.core.JdbcTemplate;
//
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.ArgumentMatchers.eq;
//import static org.mockito.Mockito.*;
//
//class PlayerServiceTest {
//
//    @InjectMocks
//    private PlayerService playerService;
//
//    @Mock
//    private JdbcTemplate jdbcTemplate;
//
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
//    }
//
//    @Test
//    void createPlayer_shouldCreateNewPlayerWhenPlayerDoesNotExist() {
//        // Arrange
//        PlayerRequestDTO playerRequestDTO = new PlayerRequestDTO();
//        playerRequestDTO.setIdNumber("12345");
//        playerRequestDTO.setName("LeBron James");
//        playerRequestDTO.setTeamName("Lakers");
//        GameStats gameStats = new GameStats();
//        gameStats.setGameDate("2021-01-01");
//        playerRequestDTO.setGameStats(gameStats);
//
//        when(jdbcTemplate.queryForObject(eq("SELECT id FROM players WHERE id_number = ?"), eq(new Object[] {"12345"}), eq(Integer.class)))
//                .thenThrow(EmptyResultDataAccessException.class);
//
//        when(jdbcTemplate.queryForObject(eq("SELECT id FROM teams WHERE name = ?"), eq(String.class), eq("Lakers")))
//                .thenReturn(String.valueOf(1)); // Assuming team ID is 1
//
//        when(jdbcTemplate.queryForObject(eq("SELECT id FROM players WHERE id_number = ?"), eq(new Object[] {"12345"}), eq(Integer.class)))
//                .thenReturn(1); // Assuming player ID is 1
//
//        // Act
//        playerService.createPlayer(playerRequestDTO);
//
//        // Assert
//        verify(jdbcTemplate, times(1)).update(eq("INSERT INTO teams (name) VALUES (?)"), eq("Lakers"));
//        verify(jdbcTemplate, times(1)).update(eq("INSERT INTO players (name, id_number, team_id) VALUES (?, ?, ?)"), eq("LeBron James"), eq("12345"), eq(1));
//        verify(jdbcTemplate, times(1)).update(eq("INSERT INTO game_stats (game_date, player_id, points, rebounds, assists, steals, blocks, fouls, turnovers, minutes_played) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)"), any(), eq(1), eq(30), eq(8), eq(7), eq(1), eq(0), eq(3), eq(3), eq(0), eq(35));
//    }
//
//    @Test
//    void createPlayer_shouldNotCreatePlayerIfAlreadyExists() {
//        // Arrange
//        PlayerRequestDTO playerRequestDTO = new PlayerRequestDTO();
//        playerRequestDTO.setIdNumber("12345");
//        playerRequestDTO.setName("LeBron James");
//        playerRequestDTO.setTeamName("Lakers");
//
//        when(jdbcTemplate.queryForObject(eq("SELECT id FROM players WHERE id_number = ?"), eq(new Object[] {"12345"}), eq(Integer.class)))
//                .thenReturn(1); // Player already exists
//
//        // Act
//        playerService.createPlayer(playerRequestDTO);
//
//        // Assert
//        verify(jdbcTemplate, times(0)).update(eq("INSERT INTO teams (name) VALUES (?)"), Optional.ofNullable(any()));
//        verify(jdbcTemplate, times(0)).update(eq("INSERT INTO players (name, id_number, team_id) VALUES (?, ?, ?)"), Optional.ofNullable(any()));
//        verify(jdbcTemplate, times(1)).update(eq("INSERT INTO game_stats (game_date, player_id, points, rebounds, assists, steals, blocks, fouls, turnovers, minutes_played) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)"), any(), eq(1), eq(30), eq(8), eq(7), eq(1), eq(0), eq(3), eq(3), eq(0), eq(35));
//    }
//
////    @Test
////    void getPlayerIdByIdNumber_shouldReturnPlayerIdIfExists() {
////        // Arrange
////        when(jdbcTemplate.queryForObject(eq("SELECT id FROM players WHERE id_number = ?"), eq(new Object[] {"12345"}), eq(Integer.class)))
////                .thenReturn(1);
////
////        // Act
////        Integer playerId = playerService.getPlayerIdByIdNumber("12345");
////
////        // Assert
////        assertEquals(1, playerId);
////    }
//
//    @Test
//    void getPlayerIdByIdNumber_shouldReturnNegativeOneIfNotExists() {
//        // Arrange
//        when(jdbcTemplate.queryForObject(eq("SELECT id FROM players WHERE id_number = ?"), eq(new Object[] {"12345"}), eq(Integer.class)))
//                .thenThrow(EmptyResultDataAccessException.class);
//
//        // Act
//        Integer playerId = playerService.getPlayerIdByIdNumber("12345");
//
//        // Assert
//        assertEquals(-1, playerId);
//    }
//}
