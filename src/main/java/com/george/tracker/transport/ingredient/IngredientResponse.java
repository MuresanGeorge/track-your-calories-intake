package com.george.tracker.transport.ingredient;

import com.george.tracker.transport.ingredient.IngredientDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class IngredientResponse {

    private List<IngredientDto> ingredients;
}
