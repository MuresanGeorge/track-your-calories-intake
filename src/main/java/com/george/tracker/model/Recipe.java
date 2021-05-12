package com.george.tracker.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "recipe")
public class Recipe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotBlank(message = "The recipe should have a name")
    private String name;

    @NotBlank(message = "The recipe should contains steps to follow")
    private String description;

//    @OneToMany(
//            mappedBy = "recipe",
//            cascade = CascadeType.ALL)
//    private List<Ingredient> ingredients = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    private Consumption consumption;

    private int calories;

    @OneToMany(mappedBy = "recipe")
    private Set<IngredientStock> ingredientStocks;

//    public void addIngredient(Ingredient ingredient) {
//        ingredients.add(ingredient);
//        ingredient.setRecipe(this);
//    }
}
