package com.george.tracker.transport;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PositiveOrZero;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class IngredientDto {

    @NotBlank(message = "The ingredient should have a name")
    private String name;

    private String brand;

    @PositiveOrZero(message = "The amount of carbohydrates should be positive")
    private int carbohydrates;

    @PositiveOrZero(message = "The amount of proteins should be positive")
    private int proteins;

    @PositiveOrZero(message = "The amount of fats should be positive")
    private int fats;

    @PositiveOrZero(message = "The amount of fibers should be positive")
    private int fibers;

//    @Positive(message = "The quantity should be greater than 0")
//    private int quantity;

}
