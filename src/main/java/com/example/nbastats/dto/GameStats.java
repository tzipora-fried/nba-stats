package com.example.nbastats.dto;

import lombok.Data;

@Data
public class GameStats {
    private String game_date;        // תאריך המשחק
    private int points;              // נקודות
    private int rebounds;            // ריבאונדים
    private int assists;             // אסיסטים
    private int steals;              // חטיפות
    private int blocks;              // חסימות
    private int fouls;               // עבירות
    private int turnovers;           // איבודי כדור
    private double minutes_played;   // דקות ששיחק
}
