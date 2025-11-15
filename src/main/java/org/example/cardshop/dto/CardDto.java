package org.example.cardshop.dto;

import lombok.Data;

@Data
public class CardDto {
    private Long id;
    private String name;
    private String description;
    private double price;
}

