package com.george.tracker.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
@AllArgsConstructor
@NoArgsConstructor
@Data
public class IngredientAmountKey implements Serializable {

    @Column(name = "ingredient_id")
    private long ingredientId;

    @Column(name = "recipe_id")
    private long recipeId;
}
