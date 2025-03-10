package com.example.nbastats.controllers;

import com.example.nbastats.dto.PlayerRequestDTO;
import com.example.nbastats.services.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/players")
public class PlayerController {

    @Autowired
    private PlayerService playerService;

    @PostMapping("/create")
    public void createPlayer(@RequestBody PlayerRequestDTO playerRequestDTO) {
        playerService.createPlayer(playerRequestDTO);
    }
}
