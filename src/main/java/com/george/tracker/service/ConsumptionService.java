package com.george.tracker.service;

import com.george.tracker.exception.ConsumptionNotFoundException;
import com.george.tracker.exception.MealNotFoundException;
import com.george.tracker.exception.RecipeNotUpdatedException;
import com.george.tracker.model.Consumption;
import com.george.tracker.model.Ingredient;
import com.george.tracker.model.IngredientStore;
import com.george.tracker.model.Macronutrient;
import com.george.tracker.model.Meal;
import com.george.tracker.model.Recipe;
import com.george.tracker.repository.ConsumptionRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ConsumptionService {

    private final ConsumptionRepository consumptionRepository;

    private final RecipeService recipeService;

    private final MealService mealService;

    public ConsumptionService(ConsumptionRepository consumptionRepository, MealService mealService,
                              RecipeService recipeService) {
        this.consumptionRepository = consumptionRepository;
        this.mealService = mealService;
        this.recipeService = recipeService;
    }

    public Consumption create(int dailyIntake, List<Meal> meals, Map<Long, Long> recipesAndQuantities) {
        Consumption newConsumption = new Consumption();
        List<Meal> mealsOfConsumption = createMealsOfConsumption(newConsumption, meals);
        Ingredient macrosFromMeals = getMacrosFromMeals(mealsOfConsumption);
        Map<Recipe, Long> recipesOfConsumptionAndQuantities = addRecipesAndQuantitiesToConsumption(newConsumption, recipesAndQuantities);
        Ingredient macrosFromRecipes = getMacrosFromCookedRecipes(recipesOfConsumptionAndQuantities);
        newConsumption.setDesiredDailyIntake(dailyIntake);
        newConsumption.setCreationDate(LocalDate.now());
        newConsumption.setFats(macrosFromMeals.getFats() + macrosFromRecipes.getFats());
        newConsumption.setCarbohydrates(macrosFromMeals.getCarbohydrates() + macrosFromRecipes.getCarbohydrates());
        newConsumption.setProteins(macrosFromMeals.getProteins() + macrosFromRecipes.getProteins());

        return consumptionRepository.save(newConsumption);
        //return a specific DTO with macros percentage and additional info about your daily intake
    }

    //TODO:think about it

    /**
     * think about what's happening if you remove a meal or a recipe from the list of meals and recipes while updating
     * an existing consumption
     */
    public Consumption updateConsumption(int newDailyIntake, List<Meal> meals, Map<Long, Long> recipesAndQuantities,
                                         Long id) {
        Consumption consumptionToBeUpdated = readById(id);
        consumptionToBeUpdated.setDesiredDailyIntake(newDailyIntake);
        updateMealsOfConsumption(meals, consumptionToBeUpdated);
        Ingredient macrosFromUpdatedMeals = getMacrosFromMeals((List<Meal>) consumptionToBeUpdated.getMeals());
        Map<Recipe, Long> recipeQuantityMap = updateRecipesOfConsumption(recipesAndQuantities, consumptionToBeUpdated);
        Ingredient macrosFromUpdatedRecipes = getMacrosFromCookedRecipes(recipeQuantityMap);
        consumptionToBeUpdated.setFats(macrosFromUpdatedMeals.getFats() + macrosFromUpdatedRecipes.getFats());
        consumptionToBeUpdated.setProteins(macrosFromUpdatedMeals.getProteins() + macrosFromUpdatedRecipes.getProteins());
        consumptionToBeUpdated.setCarbohydrates(macrosFromUpdatedMeals.getCarbohydrates() + macrosFromUpdatedRecipes.getCarbohydrates());

        return consumptionRepository.save(consumptionToBeUpdated);
    }

    public void deleteMealOfConsumption(Long consumptionId, Long mealId) {
        Consumption consumption = readById(consumptionId);
        Meal mealToBeDeleted = mealService.readById(mealId);
        Ingredient macrosFromMeal = getMacrosFromMeals(Collections.singletonList(mealToBeDeleted));
        consumption.removeMeal(mealToBeDeleted);
        consumption.setCarbohydrates(consumption.getCarbohydrates() - macrosFromMeal.getCarbohydrates());
        consumption.setProteins(consumption.getProteins() - macrosFromMeal.getProteins());
        consumption.setFats(consumption.getFats() - macrosFromMeal.getFats());

        consumptionRepository.save(consumption);
    }

    public void deleteRecipeOfConsumption(Long consumptionId, Long recipeId, long quantity) {
        Consumption consumption = readById(consumptionId);
        Recipe recipeToBeDeleted = recipeService.readById(recipeId);
        Ingredient macrosFromRecipe = getMacrosFromCookedRecipes(Collections.singletonMap(recipeToBeDeleted, quantity));
        consumption.removeRecipe(recipeToBeDeleted);
        consumption.setFats(consumption.getFats() - macrosFromRecipe.getFats());
        consumption.setProteins(consumption.getProteins() - macrosFromRecipe.getProteins());
        consumption.setCarbohydrates(consumption.getCarbohydrates() - macrosFromRecipe.getCarbohydrates());

        consumptionRepository.save(consumption);
    }

    private Consumption readById(long id) {
        return consumptionRepository.findById(id)
                .orElseThrow(() -> new ConsumptionNotFoundException("Consumption with id " + id + "not found"));
    }

    private List<Meal> createMealsOfConsumption(Consumption consumption, List<Meal> meals) {
        List<Meal> mealsInConsumption = new ArrayList<>();
        if (!meals.isEmpty()) {
            for (Meal meal : meals) {
                Meal newMeal = mealService.create(meal.getName(), new ArrayList<>(meal.getIngredientsStore()));
                consumption.addMeal(newMeal);
                mealsInConsumption.add(newMeal);
            }
        }
        return mealsInConsumption;
    }

    private Map<Recipe, Long> updateRecipesOfConsumption(Map<Long, Long> recipesAndQuantities, Consumption consumptionToBeUpdated) {
        Map<Recipe, Long> recipeQuantityMap = new HashMap<>();

        for (Map.Entry<Long, Long> pair : recipesAndQuantities.entrySet()) {
            long recipeId = pair.getKey();
            long amountOfRecipe = pair.getValue().longValue();
            Recipe recipeToBeUpdated = recipeService.readById(recipeId);
            recipeQuantityMap.put(recipeToBeUpdated, amountOfRecipe);
            consumptionToBeUpdated.addRecipe(recipeToBeUpdated);
        }
        return recipeQuantityMap;
    }

    private void updateMealsOfConsumption(List<Meal> meals, Consumption consumptionToBeUpdated) {
        for (Meal m : meals) {
            try {
                Meal mealToBeUpdated = mealService.readByName(m.getName());
                mealService.updateMeal(mealToBeUpdated.getId(), m.getName(), new ArrayList<>(m.getIngredientsStore()));
            } catch (MealNotFoundException ex) {
                Meal newMealOfConsumption = mealService.create(m.getName(), new ArrayList<>(m.getIngredientsStore()));
                consumptionToBeUpdated.addMeal(newMealOfConsumption);
            }
        }
    }

    private Map<Recipe, Long> addRecipesAndQuantitiesToConsumption(Consumption consumption, Map<Long, Long> recipesAndQuantities) {
        Map<Recipe, Long> recipeWQuantity = new HashMap<>();

        for (Map.Entry<Long, Long> pair : recipesAndQuantities.entrySet()) {
            Recipe r = recipeService.readById(pair.getKey());
            if (r.isUpdated()) {
                consumption.addRecipe(r);
                recipeWQuantity.put(r, pair.getValue());
            } else {
                throw new RecipeNotUpdatedException("The recipe with id " + r.getId() + "not updated yet");
            }
        }
        return recipeWQuantity;
    }

    private Ingredient getMacrosFromMeals(List<Meal> meals) {
        int totalProteinsInMeals = 0;
        int totalFatsInMeals = 0;
        int totalCarbohydratesInMeals = 0;
        for (Meal meal : meals) {
            for (IngredientStore is : meal.getIngredientsStore()) {
                totalCarbohydratesInMeals += mealService.calculateMacrosFromIngredientStore(Macronutrient.CARBOHYDRATE, is);
                totalFatsInMeals += mealService.calculateMacrosFromIngredientStore(Macronutrient.FAT, is);
                totalProteinsInMeals += mealService.calculateMacrosFromIngredientStore(Macronutrient.PROTEIN, is);
            }
        }
        return Ingredient.builder()
                .carbohydrates(totalCarbohydratesInMeals)
                .fats(totalFatsInMeals)
                .proteins(totalProteinsInMeals)
                .build();
    }

    private Ingredient getMacrosFromCookedRecipes(Map<Recipe, Long> recipeAndQuantity) {
        int totalFatsInRecipes = 0;
        int totalProteinsInRecipes = 0;
        int totalCarbohydratesInRecipes = 0;
        for (Map.Entry<Recipe, Long> pair : recipeAndQuantity.entrySet()) {
            totalFatsInRecipes += getFatsFromAmountOfRecipe(pair);
            totalCarbohydratesInRecipes += getCarbohydratesFromAmountOfRecipe(pair);
            totalProteinsInRecipes += getProteinsFromAmountOfRecipe(pair);
        }
        return Ingredient.builder()
                .carbohydrates(totalCarbohydratesInRecipes)
                .proteins(totalProteinsInRecipes)
                .fats(totalFatsInRecipes)
                .build();
    }

    private int getCarbohydratesFromAmountOfRecipe(Map.Entry<Recipe, Long> pair) {
        return (recipeService.getMacrosOfARecipe(pair.getKey(), Macronutrient.CARBOHYDRATE) * pair.getValue().intValue()) /
                pair.getKey().getWeight();
    }

    private int getProteinsFromAmountOfRecipe(Map.Entry<Recipe, Long> pair) {
        return (recipeService.getMacrosOfARecipe(pair.getKey(), Macronutrient.PROTEIN) * pair.getValue().intValue()) /
                pair.getKey().getWeight();
    }

    private int getFatsFromAmountOfRecipe(Map.Entry<Recipe, Long> pair) {
        return (recipeService.getMacrosOfARecipe(pair.getKey(), Macronutrient.FAT) * pair.getValue().intValue()) /
                pair.getKey().getWeight();
    }
}
