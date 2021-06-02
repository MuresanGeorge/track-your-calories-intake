package com.george.tracker.service;

import com.george.tracker.exception.IngredientStockNotFoundException;
import com.george.tracker.model.Ingredient;
import com.george.tracker.model.IngredientAmountKey;
import com.george.tracker.model.IngredientStock;
import com.george.tracker.model.Recipe;
import com.george.tracker.repository.IngredientStockRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class IngredientStockService {

    private final IngredientStockRepository ingredientStockRepository;


    public IngredientStockService(IngredientStockRepository ingredientStockRepository) {
        this.ingredientStockRepository = ingredientStockRepository;
    }


    public IngredientStock create(Ingredient ingredient, Recipe recipe, int quantity) {
        IngredientStock newIngredientStock = IngredientStock.builder()
                .id(new IngredientAmountKey(ingredient.getId(), recipe.getId()))
                .ingredient(ingredient)
                .recipe(recipe)
                .quantity(quantity)
                .build();

        return ingredientStockRepository.save(newIngredientStock);
    }

    public IngredientStock readIngredientStock(Long ingredientId, Long recipeId) {
        return ingredientStockRepository.findById(new IngredientAmountKey(ingredientId, recipeId))
                .orElseThrow(() -> new IngredientStockNotFoundException("Ingredient stock not found"));
    }

    public List<IngredientStock> readIngredientStocksOfRecipe(Long recipeId) {
        return ingredientStockRepository.findByRecipeId(recipeId);
    }
}
