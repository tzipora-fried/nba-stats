package com.example.nbastats.controllers;

import com.example.nbastats.dto.PlayerRequestDTO;
import com.example.nbastats.services.PlayerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/players")
public class PlayerController {

    private static final Logger logger = LoggerFactory.getLogger(PlayerController.class);

    @Autowired
    private PlayerService playerService;

    @PostMapping("/create")
    public void createPlayer(@RequestBody PlayerRequestDTO playerRequestDTO) {
        try {
            playerService.createPlayer(playerRequestDTO);
        } catch (Exception e) {
            logger.error("Error creating player: {}", e.getMessage());
            throw e;
        }
    }
}
