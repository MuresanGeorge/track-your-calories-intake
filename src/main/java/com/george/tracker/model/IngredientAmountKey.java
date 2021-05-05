package com.george.tracker.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
@Data
public class IngredientAmountKey implements Serializable {

    @Column(name = "ingredient_id")
    private long ingredientId;

    @Column(name = "recipe_id")
    private long recipeId;
}
