package com.example.nbastats.entities;

import lombok.Data;

@Data
public class PlayerEntity {
    private Long id;
    private String name;
    private String idNumber;
    private Long teamId;
}
