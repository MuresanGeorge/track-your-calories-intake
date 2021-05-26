package com.george.tracker.transport.recipe;

import com.george.tracker.transport.recipe.RecipeDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RecipeResponse {

    private List<RecipeDto> recipes;
}
