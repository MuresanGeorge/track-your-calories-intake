package com.george.tracker.service;

import com.george.tracker.exception.IngredientStoreNotFoundException;
import com.george.tracker.exception.MealNotFoundException;
import com.george.tracker.model.Ingredient;
import com.george.tracker.model.IngredientStore;
import com.george.tracker.model.Meal;
import com.george.tracker.repository.MealRepository;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Service
public class MealService {

    private final MealRepository mealRepository;

    private final IngredientService ingredientService;

    private final IngredientStoreService ingredientStoreService;

    public MealService(MealRepository mealRepository, IngredientService ingredientService,
                       IngredientStoreService ingredientStoreService) {
        this.mealRepository = mealRepository;
        this.ingredientService = ingredientService;
        this.ingredientStoreService = ingredientStoreService;
    }


    public void create(String mealName, Map<Long, Long> ingredientsQuantities) {
        Meal newMeal = new Meal();
        newMeal.setName(mealName);
        Set<IngredientStore> ingredientsInMeal;
        if (ingredientsQuantities != null) {
            ingredientsInMeal = createIngredientsStoreInMeal(ingredientsQuantities, newMeal);
            newMeal.setIngredientsStore(ingredientsInMeal);
        }
        mealRepository.save(newMeal);
    }

    public Meal updateMeal(long mealId, String mealName, Map<Long, Long> ingredientsQuantities) {
        Meal mealToBeUpdated = readById(mealId);

        mealToBeUpdated.setName(mealName);
        updateIngredientsStore(mealToBeUpdated, ingredientsQuantities);

        return mealRepository.save(mealToBeUpdated);
    }

    private void updateIngredientsStore(Meal meal, Map<Long, Long> ingredientsQuantities) {
        ingredientsQuantities.forEach((ingredientId, ingredientAmount) -> {
            try {
                IngredientStore ingredientStoreToBeUpdated = ingredientStoreService
                        .readByIngredientAndMeal(ingredientId, meal.getId());
                ingredientStoreToBeUpdated.setAmount(ingredientAmount.intValue());
            } catch (IngredientStoreNotFoundException ex) {
                createIngredientStoreIfNotExist(ingredientId, meal, ingredientAmount.intValue());
            }
        });
    }

    private void createIngredientStoreIfNotExist(Long ingredientId, Meal meal, int amount) {
        Ingredient ingredientToBeStored = ingredientService.readIngredient(ingredientId);
        ingredientStoreService.create(ingredientToBeStored, meal, amount);
    }

    private Set<IngredientStore> createIngredientsStoreInMeal(Map<Long, Long> ingredientsQuantities, Meal meal) {
        Set<IngredientStore> ingredientsStore = new HashSet<>();

        for (Map.Entry<Long, Long> pair : ingredientsQuantities.entrySet()) {
            Ingredient ingredient = ingredientService.readIngredient(pair.getKey());
            IngredientStore ingredientStore = new IngredientStore();
            ingredientStore.setIngredient(ingredient);
            ingredientStore.setAmount(pair.getValue().intValue());
            ingredientStore.setMeal(meal);
            ingredientsStore.add(ingredientStore);
        }
        return ingredientsStore;
    }

    private Meal readById(long mealId) {
        return mealRepository.findById(mealId)
                .orElseThrow(() -> new MealNotFoundException("Meal with id " + mealId + " not found"));
    }
}
