package com.george.tracker.transport.meal;

import com.george.tracker.transport.ingredient.IngredientStoreDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MealDto {

    @NotBlank(message = "The meal should have a name")
    private String name;

    private List<IngredientStoreDto> ingredientsStore;
}
