package com.george.tracker.transport;

import com.george.tracker.transport.recipe.RecipeDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConsumptionDto {

    @PositiveOrZero(message = "Daily intake should be 0 on fasting days and greater than 0 on the other days")
    private int desiredDailyIntake;

    private int carbohydratesPercentage;

    private int proteinsPercentage;

    private int fatsPercentage;

    /**
     * add your first meal of the day or add later in UI
     */
    @Valid
    private List<MealDto> meals;

    /**
     * add a recipe from existing ones or add a new recipe to your kitchen in UI
     */
    @Valid
    private List<RecipeDto> recipes;
}
