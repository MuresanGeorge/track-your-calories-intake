package com.george.tracker.service;

import com.george.tracker.exception.ConsumptionNotFoundException;
import com.george.tracker.model.Consumption;
import com.george.tracker.model.Meal;
import com.george.tracker.model.Recipe;
import com.george.tracker.repository.ConsumptionRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class ConsumptionService {

    private final ConsumptionRepository consumptionRepository;

    private final MealService mealService;

    private final RecipeService recipeService;

    public ConsumptionService(ConsumptionRepository consumptionRepository, MealService mealService,
                              RecipeService recipeService) {
        this.consumptionRepository = consumptionRepository;
        this.mealService = mealService;
        this.recipeService = recipeService;
    }

    public void create(int dailyIntake, List<Meal> meals, List<Recipe> recipes) {
        Consumption consumption = new Consumption();
        List<Meal> mealsOfTheDay = new ArrayList<>();
        List<Recipe> recipesOfTheDay = new ArrayList<>();

        if (!meals.isEmpty()) {
            for (Meal m : meals) {
                mealService.create(m);
                mealsOfTheDay.add(m);
            }
        }
        if (!recipes.isEmpty()) {
            for (Recipe r : recipes) {
                recipeService.create(r.getName(), r.getDescription(), r.getIngredients());
                recipesOfTheDay.add(r);
            }
        }
        consumption.setDesiredDailyIntake(dailyIntake);
        consumption.setCreationDate(LocalDate.now());
        consumption.setMeals(mealsOfTheDay);
        consumption.setRecipes(recipesOfTheDay);
        consumptionRepository.save(consumption);
    }

    //TODO verify this
    public int getTotalAmountOfCalories(LocalDate creationDate) {
        Consumption consumption = consumptionRepository.findByCreationDate(creationDate)
                .orElseThrow(() -> new ConsumptionNotFoundException("Consumption with " + creationDate + "not found"));
        int totalAmountOfCalories = 0;
        for (Recipe r : consumption.getRecipes()) {
            totalAmountOfCalories += recipeService.getTotalNumberOfCaloriesInRecipe(r);
        }
        for (Meal m : consumption.getMeals()) {
            totalAmountOfCalories += mealService.getTotalNumberOfCaloriesInMeal(m);
        }
        return totalAmountOfCalories;
    }
}
