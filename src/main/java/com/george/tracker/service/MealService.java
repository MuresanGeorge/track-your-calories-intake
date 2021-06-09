package com.george.tracker.service;

import com.george.tracker.exception.IngredientStoreNotFoundException;
import com.george.tracker.exception.MealNotFoundException;
import com.george.tracker.model.Ingredient;
import com.george.tracker.model.IngredientStore;
import com.george.tracker.model.Macronutrient;
import com.george.tracker.model.Meal;
import com.george.tracker.repository.MealRepository;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
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


    public Meal create(String mealName, List<IngredientStore> ingredientsQuantities) {
        Meal newMeal = new Meal();
        newMeal.setName(mealName);
        Set<IngredientStore> ingredientsInMeal = createIngredientsStoreInMeal(ingredientsQuantities, newMeal);
        newMeal.setIngredientsStore(ingredientsInMeal);

        return mealRepository.save(newMeal);
    }

    //TODO:think about it

    /**
     * think about what's happening if you remove an ingredient store from the list of ingredients store while updating
     * an existing meal
     */
    public Meal updateMeal(long mealId, String mealName, List<IngredientStore> ingredientsQuantities) {
        Meal mealToBeUpdated = readById(mealId);
        mealToBeUpdated.setName(mealName);
        updateIngredientsStore(mealToBeUpdated, ingredientsQuantities);

        return mealRepository.save(mealToBeUpdated);
    }

    public Meal readByName(String mealName) {
        return mealRepository.findByName(mealName)
                .orElseThrow(() -> new MealNotFoundException("Meal with name " + mealName + " not found"));
    }

    public Meal readById(long mealId) {
        return mealRepository.findById(mealId)
                .orElseThrow(() -> new MealNotFoundException("Meal with id " + mealId + " not found"));
    }

    public int calculateMacrosFromIngredientStore(Macronutrient macronutrient, IngredientStore ingredientStore) {
        int macroValue;
        switch (macronutrient) {
            case FAT:
                macroValue = ingredientStoreService.calculateTotalFats(ingredientStore);
                break;
            case PROTEIN:
                macroValue = ingredientStoreService.calculateTotalProteins(ingredientStore);
                break;
            case CARBOHYDRATE:
                macroValue = ingredientStoreService.calculateTotalCarbohydrates(ingredientStore);
                break;
            default:
                macroValue = 0;
        }
        return macroValue;
    }

    private void updateIngredientsStore(Meal meal, List<IngredientStore> ingredientsQuantities) {
        ingredientsQuantities.forEach(is -> {
            try {
                ingredientStoreService.updateIngredientStore(is.getIngredient().getId(), meal.getId(), is.getAmount());
            } catch (IngredientStoreNotFoundException ex) {
                createIngredientStoreIfNotExist(is.getIngredient().getId(), meal, is.getAmount());
            }
        });
    }

    private void createIngredientStoreIfNotExist(Long ingredientId, Meal meal, int amount) {
        Ingredient ingredientToBeStored = ingredientService.readIngredient(ingredientId);
        ingredientStoreService.create(ingredientToBeStored, meal, amount);
    }

    private Set<IngredientStore> createIngredientsStoreInMeal(List<IngredientStore> ingredientsQuantities, Meal newMeal) {
        Set<IngredientStore> ingredientsInMeal = new HashSet<>();
        if (!ingredientsQuantities.isEmpty()) {
            for (IngredientStore is : ingredientsQuantities) {
                Ingredient ingredient = ingredientService.readIngredient(is.getIngredient().getId());
                IngredientStore ingredientStore = new IngredientStore();
                ingredientStore.setIngredient(ingredient);
                ingredientStore.setAmount(is.getAmount());
                ingredientStore.setMeal(newMeal);
                ingredientsInMeal.add(ingredientStore);
            }
        }
        return ingredientsInMeal;
    }
}
