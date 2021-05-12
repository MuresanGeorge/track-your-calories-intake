package com.george.tracker.service;

import com.george.tracker.model.Ingredient;
import com.george.tracker.model.Meal;
import com.george.tracker.repository.MealRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MealService {

    private final MealRepository mealRepository;

    private final IngredientService ingredientService;

    public MealService(MealRepository mealRepository, IngredientService ingredientService) {
        this.mealRepository = mealRepository;
        this.ingredientService = ingredientService;
    }

    public void create(String mealName, List<Ingredient> ingredients) {
        Meal meal = new Meal();
        meal.setName(mealName);

//        if (!ingredients.isEmpty()) {
//            for (Ingredient i : ingredients) {
//                meal.addIngredient(i);
////                ingredientService.create(i);
//            }
//        }
        mealRepository.save(meal);
    }

//    public int getTotalNumberOfCaloriesInMeal(Meal meal) {
//        return ingredientService.getTotalNumberOfCalories(meal.getIngredients());
//    }
}
