package com.george.tracker.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Ingredient {

    private long id;

    private String carbohydrates;

    private String proteins;

    private String fats;
}
