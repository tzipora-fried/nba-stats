package com.example.nbastats.dto;

import lombok.Data;

@Data
public class PlayerRequestDTO {
    private String name;         // שם השחקן
    private String id_number;    // תעודת זהות של השחקן
    private String team_name;    // שם הקבוצה
    private GameStats game_stats; // הסטטיסטיקות של השחקן במשחק
}
