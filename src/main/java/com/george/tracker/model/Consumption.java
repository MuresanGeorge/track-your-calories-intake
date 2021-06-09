package com.george.tracker.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.Positive;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "consumption")
public class Consumption {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Positive(message = "The daily intake should be greater than 0")
    private int desiredDailyIntake;

    @FutureOrPresent(message = "The consumption date should be today or in the future")
    private LocalDate creationDate;

    /**
     * HIT the MACROS IDEA
     */
    private int carbohydrates;

    private int proteins;

    private int fats;

    @ManyToMany
    @JoinTable(name = "consumption_meal",
            joinColumns = @JoinColumn(name = "consumption_id"),
            inverseJoinColumns = @JoinColumn(name = "meal_id")
    )
    private Set<Meal> meals = new HashSet<>();

    @ManyToMany
    @JoinTable(name = "consumption_recipe",
            joinColumns = @JoinColumn(name = "consumption_id"),
            inverseJoinColumns = @JoinColumn(name = "recipe_id")
    )
    private Set<Recipe> recipes = new HashSet<>();

    public void addMeal(Meal meal) {
        meals.add(meal);
        meal.getConsumptions().add(this);
    }

    public void addRecipe(Recipe recipe) {
        recipes.add(recipe);
        recipe.getConsumptions().add(this);
    }

    public void removeRecipe(Recipe recipe) {
        recipes.remove(recipe);
        recipe.getConsumptions().remove(this);
    }

    public void removeMeal(Meal meal) {
        meals.remove(meal);
        meal.getConsumptions().remove(this);
    }
}
