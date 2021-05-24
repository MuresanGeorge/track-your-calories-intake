package com.george.tracker.transport;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RecipeDto {

    @NotBlank(message = "The recipe should have a name")
    private String name;

    @NotBlank(message = "The recipe should contains steps to follow")
    private String description;

    private List<IngredientDto> ingredients;

}
