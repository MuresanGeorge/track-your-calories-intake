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


    public IngredientStore create(Ingredient ingredient, Meal meal, int amount) {
        IngredientStore newIngredientStore = new IngredientStore();
        newIngredientStore.setAmount(amount);
        newIngredientStore.setIngredient(ingredient);
        newIngredientStore.setMeal(meal);

        return ingredientStoreRepository.save(newIngredientStore);
    }

    public void deleteIngredientStore(Long ingredientId, Long mealId) {
        IngredientStore ingredientStoreToBeDeleted = readByIngredientAndMeal(ingredientId, mealId);
        ingredientStoreRepository.delete(ingredientStoreToBeDeleted);
    }

    public void updateIngredientStore(Long ingredientId, Long mealId, int amount) {
        IngredientStore ingredientStoreToBeUpdated = readByIngredientAndMeal(ingredientId, mealId);
        ingredientStoreToBeUpdated.setAmount(amount);
    }

    //TODO: document this
    public int calculateTotalFats(IngredientStore ingredientStore) {
        int ingredientAmount = ingredientStore.getAmount();
        int fatsPer100G = ingredientStore.getIngredient().getFats();

        return (ingredientAmount * fatsPer100G) / 100;
    }

    public int calculateTotalProteins(IngredientStore ingredientStore) {
        int ingredientAmount = ingredientStore.getAmount();
        int proteinsPer100G = ingredientStore.getIngredient().getProteins();

        return (ingredientAmount * proteinsPer100G) / 100;
    }

    public int calculateTotalCarbohydrates(IngredientStore ingredientStore) {
        int ingredientAmount = ingredientStore.getAmount();
        int carbohydratesPer100G = ingredientStore.getIngredient().getCarbohydrates();

        return (ingredientAmount * carbohydratesPer100G) / 100;
    }

    private IngredientStore readByIngredientAndMeal(Long ingredientId, Long mealId) {
        return ingredientStoreRepository.findByIngredientIdAndMealId(ingredientId, mealId)
                .orElseThrow(() -> new IngredientStoreNotFoundException
                        ("Ingredient store with meal id " + mealId + " and ingredient id " + ingredientId + "not found"));
    }
}
