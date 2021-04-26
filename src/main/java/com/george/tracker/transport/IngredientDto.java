package com.george.tracker.transport;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class IngredientDto {

    @NotBlank(message = "The ingredient should have a name")
    private String name;

    private String carbohydrates;

    private String proteins;

    private String fats;

    private String fibers;

}
