package com.example.nbastats.entities;

import lombok.Data;

@Data
public class PlayerEntity {
    private Long id;                // מזהה ייחודי
    private String name;            // שם השחקן
    private String id_number;       // תעודת זהות של השחקן
    private Long teamId;       // שם הקבוצה
}
