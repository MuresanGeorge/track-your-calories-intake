package com.george.tracker.transport.recipe;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RecipeQuantityDto {

    @NotNull(message = "The recipe id should not be null")
    private int recipeId;

    @PositiveOrZero(message = "The quantity of the recipe should not be less than 0")
    private int quantity;
}
