package com.george.tracker.transport.ingredient;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class IngredientStoreDto {

    private long ingredientId;

    private int amount;
}
