package com.example.nbastats.entities;

import lombok.Data;

@Data
public class GameStatsEntity {
    private Long player_id;
    private String game_date;
    private int points;
    private int rebounds;
    private int assists;
    private int steals;
    private int blocks;
    private int fouls;
    private int turnovers;
    private double minutes_played;
}
