package com.example.nbastats.dto;

import lombok.Data;

@Data
public class PlayerRequestDTO {
    private String name;
    private String idNumber;
    private String teamName;
    private GameStats gameStats;
}
