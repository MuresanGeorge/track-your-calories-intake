package com.george.tracker.transport.consumption;

import com.george.tracker.transport.meal.MealDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConsumptionDto {

    private long id;

    @PositiveOrZero(message = "Daily intake should be 0 on fasting days and greater than 0 on the other days")
    private int desiredDailyIntake;

    /**
     * add your first meal of the day or add later in UI
     */
    @Valid
    private List<MealDto> meals;

    /**
     * add a recipe from existing ones in UI
     */
    private Map<Long, Long> recipesWithQuantities;
}
