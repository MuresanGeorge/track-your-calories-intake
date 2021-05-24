package com.george.tracker.transport.usda;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FoodNutrientUsda {

    private String nutrientName;

    private String nutrientNumber;

    private int value;
}
