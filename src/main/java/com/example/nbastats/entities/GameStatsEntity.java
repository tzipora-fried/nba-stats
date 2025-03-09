package com.example.nbastats.entities;

import lombok.Data;

@Data
public class GameStatsEntity {
    private Long playerId;
    private String gameDate;
    private int points;
    private int rebounds;
    private int assists;
    private int steals;
    private int blocks;
    private int fouls;
    private int turnovers;
    private double minutesPlayed;
}
