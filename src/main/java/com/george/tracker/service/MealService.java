package com.george.tracker.service;

import com.george.tracker.model.Meal;
import com.george.tracker.repository.MealRepository;
import org.springframework.stereotype.Service;

@Service
public class MealService {

    private final MealRepository mealRepository;

    private final IngredientService ingredientService;

    public MealService(MealRepository mealRepository, IngredientService ingredientService) {
        this.mealRepository = mealRepository;
        this.ingredientService = ingredientService;
    }

    public void create(Meal meal) {
        mealRepository.save(meal);
    }

    public int getTotalNumberOfCaloriesInMeal(Meal meal) {
        return ingredientService.getTotalNumberOfCalories(meal.getIngredients());
    }
}
