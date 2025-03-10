package com.example.nbastats.services;

import com.example.nbastats.dto.GameStats;
import com.example.nbastats.dto.PlayerRequestDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PlayerServiceTest {

    @Mock
    private JdbcTemplate jdbcTemplate;

    @InjectMocks
    private PlayerService playerService;

    private PlayerRequestDTO playerRequestDTO;

    @BeforeEach
    void setUp() {
        playerRequestDTO = new PlayerRequestDTO();
        playerRequestDTO.setId_number("12345");
        playerRequestDTO.setName("LeBron James");
        playerRequestDTO.setTeam_name("Lakers");

        GameStats gameStats = new GameStats();
        gameStats.setGame_date("2024-03-10");
        gameStats.setPoints(30);
        gameStats.setRebounds(8);
        gameStats.setAssists(10);
        gameStats.setSteals(2);
        gameStats.setBlocks(1);
        gameStats.setFouls(3);
        gameStats.setTurnovers(4);
        gameStats.setMinutes_played(35);

        playerRequestDTO.setGame_stats(gameStats);
    }

    @Test
    void createPlayer_ShouldReturnError_WhenPlayerAlreadyExists() {
        when(jdbcTemplate.queryForObject(anyString(), any(), eq(Integer.class))).thenReturn(1);

        String result = playerService.createPlayer(playerRequestDTO);

        assertEquals("Player with this ID number already exists!", result);
        verify(jdbcTemplate, times(1)).queryForObject(anyString(), any(), eq(Integer.class));
    }

    @Test
    void createPlayer_ShouldInsertPlayerAndReturnSuccess() {
        when(jdbcTemplate.queryForObject(anyString(), any(), eq(Integer.class)))
                .thenReturn(0) // Player doesn't exist
                .thenReturn(-1) // Team doesn't exist
                .thenReturn(10) // New team ID
                .thenReturn(20); // Player ID

        doNothing().when(jdbcTemplate).update(anyString(), Optional.ofNullable(any()));

        String result = playerService.createPlayer(playerRequestDTO);

        assertEquals("Player and game stats inserted successfully!", result);
        verify(jdbcTemplate, times(3)).queryForObject(anyString(), any(), eq(Integer.class));
        verify(jdbcTemplate, times(3)).update(anyString(), Optional.ofNullable(any()));
    }

    @Test
    void getTeamIdByName_ShouldReturnTeamId_WhenExists() {
        when(jdbcTemplate.queryForObject(anyString(), eq(Integer.class), any()))
                .thenReturn(5);

        int teamId = playerService.getTeamIdByName("Lakers");

        assertEquals(5, teamId);
        verify(jdbcTemplate, times(1)).queryForObject(anyString(), eq(Integer.class), any());
    }

    @Test
    void getTeamIdByName_ShouldReturnMinusOne_WhenTeamDoesNotExist() {
        when(jdbcTemplate.queryForObject(anyString(), eq(Integer.class), any()))
                .thenThrow(new EmptyResultDataAccessException(1));

        int teamId = playerService.getTeamIdByName("Unknown Team");

        assertEquals(-1, teamId);
        verify(jdbcTemplate, times(1)).queryForObject(anyString(), eq(Integer.class), any());
    }
}
