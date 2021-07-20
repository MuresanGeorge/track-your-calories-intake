package com.george.tracker.transport.ingredient;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class IngredientStockDto {

    @NotNull
    private long ingredientId;

    @NotNull
    private long recipeId;

    @PositiveOrZero(message = "The quantity should be greater than 0 or equal to 0")
    private int quantity;
}
