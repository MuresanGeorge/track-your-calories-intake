package com.george.tracker.service;

import com.george.tracker.exception.IngredientStockNotFoundException;
import com.george.tracker.exception.RecipeNotFoundException;
import com.george.tracker.model.Ingredient;
import com.george.tracker.model.IngredientAmountKey;
import com.george.tracker.model.IngredientStock;
import com.george.tracker.model.Macronutrient;
import com.george.tracker.model.Recipe;
import com.george.tracker.repository.RecipeRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public class RecipeService {

    private final RecipeRepository recipeRepository;

    private final IngredientService ingredientService;

    private final IngredientStockService ingredientStockService;

    public RecipeService(RecipeRepository recipeRepository, IngredientService ingredientService,
                         IngredientStockService ingredientStockService) {
        this.recipeRepository = recipeRepository;
        this.ingredientService = ingredientService;
        this.ingredientStockService = ingredientStockService;
    }

    public void create(String name, String description, Map<Long, Long> ingredientsQuantities) {
        Recipe newRecipe = Recipe.builder()
                .calories(0)
                .description(description)
                .name(name)
                .ingredientStocks(new HashSet<>())
                .isUpdated(false)
                .weight(0)
                .build();

        if (ingredientsQuantities != null) {
            Set<IngredientStock> ingredientStockSet = createIngredientStocksOfRecipe(ingredientsQuantities, newRecipe);
            newRecipe.setIngredientStocks(ingredientStockSet);
            newRecipe.setCalories(ingredientService.calculateTotalCalories(ingredientsQuantities));
        }
        recipeRepository.save(newRecipe);
    }

    //TODO:think about it

    public List<Recipe> readRecipes(Long id, String name, String ingredientName, List<Long> ingredients) {
        List<Recipe> allRecipes = recipeRepository.findAll();

        allRecipes = filterById(id, allRecipes);
        allRecipes = filterByName(name, allRecipes);
        allRecipes = filterByIngredientName(ingredientName, allRecipes);
        allRecipes = filterByIngredients(ingredients, allRecipes);

        return allRecipes;
    }

    /**
     * think about what's happening if you remove an ingredient stock from the list of ingredients stocks while updating
     * an existing recipe
     */
    public Recipe updateRecipe(long recipeId, String name, String description, Map<Long, Long> ingredientsQuantities) {
        Recipe recipeToBeUpdated = recipeRepository.findById(recipeId)
                .orElseThrow(() -> new RecipeNotFoundException("Recipe with id " + recipeId + "not found"));

        recipeToBeUpdated.setName(name);
        recipeToBeUpdated.setDescription(description);
        updateIngredientStocks(ingredientsQuantities, recipeToBeUpdated);
        recipeToBeUpdated.setCalories(ingredientService.calculateTotalCalories(ingredientsQuantities));
        return recipeRepository.save(recipeToBeUpdated);
    }

    public Integer updateCalories(Integer weight, Long id) {
        Recipe recipeToBeUpdated = readById(id);
        recipeToBeUpdated.setCalories(calculateCaloriesPer100G(recipeToBeUpdated, weight));
        recipeToBeUpdated.setUpdated(true);
        recipeToBeUpdated.setWeight(weight);
        Recipe recipeAfterUpdate = recipeRepository.save(recipeToBeUpdated);
        return recipeAfterUpdate.getCalories();
    }

    public Recipe readById(Long id) {
        return recipeRepository.findById(id)
                .orElseThrow(() -> new RecipeNotFoundException("Recipe with id " + id + "not found"));
    }

    public int getMacrosOfARecipe(Recipe recipe, Macronutrient macronutrient) {
        int macro;
        switch (macronutrient) {
            case CARBOHYDRATE:
                macro = ingredientStockService.getTotalCarbohydrates(recipe.getIngredientStocks());
                break;
            case PROTEIN:
                macro = ingredientStockService.getTotalProteins(recipe.getIngredientStocks());
                break;
            case FAT:
                macro = ingredientStockService.getTotalFats(recipe.getIngredientStocks());
                break;
            default:
                macro = 0;
                break;
        }
        return macro;
    }

    private int getTotalCaloriesInARecipe(Set<IngredientStock> ingredientStocks) {
        Map<Long, Long> ingredientsQuantities = new HashMap<>();
        ingredientStocks.forEach(ingredientStock -> ingredientsQuantities.put(ingredientStock.getIngredient().getId(),
                Long.valueOf(ingredientStock.getQuantity())));
        return ingredientService.calculateTotalCalories(ingredientsQuantities);
    }

    private List<Recipe> filterByName(String name, List<Recipe> recipes) {
        if (name == null) {
            return recipes;
        }
        return recipeRepository.findByNameContainingIgnoreCase(name);
    }

    private List<Recipe> filterByIngredients(List<Long> ingredients, List<Recipe> allRecipes) {
        //TODO : findByIngredientStocks_IngredientIdIn can be improved for a perfect matching
        if (ingredients == null) {
            return allRecipes;
        }
        Set<Recipe> recipesThatIncludeIngredients = recipeRepository.findByIngredientStocks_IngredientIdIn(ingredients);
        List<Recipe> recipesThatIncludeIngredientsList = new ArrayList<>();
        recipesThatIncludeIngredients.forEach(recipe -> recipesThatIncludeIngredientsList.add(recipe));
        return recipesThatIncludeIngredientsList;
    }

    private List<Recipe> filterByIngredientName(String ingredientName, List<Recipe> recipes) {
        if (ingredientName == null) {
            return recipes;
        }
        return readByIngredientName(ingredientName);
    }

    private List<Recipe> readByIngredientName(String ingredientName) {
        return recipeRepository.findByIngredientStocks_IngredientNameContains(ingredientName);
    }

    private List<Recipe> filterById(Long id, List<Recipe> recipes) {
        if (id == null) {
            return recipes;
        }
        return Collections.singletonList(readById(id));
    }

    private Set<IngredientStock> createIngredientStocksOfRecipe(Map<Long, Long> ingredientsQuantities, Recipe recipe) {
        Set<IngredientStock> ingredientStocks = new HashSet<>();
        for (Map.Entry<Long, Long> pair : ingredientsQuantities.entrySet()) {
            IngredientStock ingredientStock = IngredientStock.builder()
                    .id(new IngredientAmountKey(pair.getKey(), recipe.getId()))
                    .ingredient(ingredientService.readIngredient(pair.getKey()))
                    .quantity(pair.getValue().intValue())
                    .recipe(recipe)
                    .build();
            ingredientStocks.add(ingredientStock);
        }
        return ingredientStocks;
    }

    private void updateIngredientStocks(Map<Long, Long> ingredientsQuantities, Recipe recipeToBeUpdated) {
        for (Map.Entry<Long, Long> pair : ingredientsQuantities.entrySet()) {
            long ingredientId = pair.getKey();
            int quantity = pair.getValue().intValue();
            try {
                IngredientStock ingredientStockToBeUpdated = ingredientStockService.readIngredientStock(ingredientId,
                        recipeToBeUpdated.getId());
                ingredientStockToBeUpdated.setQuantity(quantity);
            } catch (IngredientStockNotFoundException ex) {
                createIngredientStockIfNotExist(ingredientId, recipeToBeUpdated, quantity);
            }
        }
    }

    private void createIngredientStockIfNotExist(long ingredientId, Recipe recipe, int quantity) {
        Ingredient ingredientToBeStocked = ingredientService.readIngredient(ingredientId);
        ingredientStockService.create(ingredientToBeStocked, recipe, quantity);
    }

    private int calculateCaloriesPer100G(Recipe recipe, Integer weight) {
        int totalCaloriesInAllIngredients = recipe.getCalories();
        return ((100 * totalCaloriesInAllIngredients) / weight.intValue());
    }

}
