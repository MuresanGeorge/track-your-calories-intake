package com.george.tracker.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.Positive;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

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

    @OneToMany(
            mappedBy = "consumption",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    private List<Meal> meals = new ArrayList<>();

    @OneToMany(
            mappedBy = "consumption",
            cascade = CascadeType.ALL)
    private List<Recipe> recipes = new ArrayList<>();

    public void addMeal(Meal meal) {
        meals.add(meal);
        meal.setConsumption(this);
    }

    public void addRecipe(Recipe recipe) {
        recipes.add(recipe);
        recipe.setConsumption(this);
    }

    public void removeMeal(Meal meal) {
        meals.remove(meal);
        meal.setConsumption(null);
    }
}
