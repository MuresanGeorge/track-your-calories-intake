package com.george.tracker.service;

import com.george.tracker.exception.IngredientStoreNotFoundException;
import com.george.tracker.model.Ingredient;
import com.george.tracker.model.IngredientStore;
import com.george.tracker.model.Meal;
import com.george.tracker.repository.IngredientStoreRepository;
import org.springframework.stereotype.Service;

@Service
public class IngredientStoreService {

    private final IngredientStoreRepository ingredientStoreRepository;

    public IngredientStoreService(IngredientStoreRepository ingredientStoreRepository) {
        this.ingredientStoreRepository = ingredientStoreRepository;
    }


    public IngredientStore readByIngredientAndMeal(Long ingredientId, Long mealId) {
        return ingredientStoreRepository.findByIngredientIdAndMealId(ingredientId, mealId)
                .orElseThrow(() -> new IngredientStoreNotFoundException
                        ("Ingredient store with meal id " + mealId + " and ingredient id " + ingredientId + "not found"));
    }

    public IngredientStore create(Ingredient ingredient, Meal meal, int amount) {
        IngredientStore newIngredientStore = new IngredientStore();
        newIngredientStore.setAmount(amount);
        newIngredientStore.setIngredient(ingredient);
        newIngredientStore.setMeal(meal);

        return ingredientStoreRepository.save(newIngredientStore);
    }
}
