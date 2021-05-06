package com.george.tracker.transport;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MealDto {

    private String name;

    @NotNull
    @Valid
    private List<IngredientDto> ingredients;
}
