package com.george.tracker.service;

import com.george.tracker.exception.IngredientStockNotFoundException;
import com.george.tracker.model.Ingredient;
import com.george.tracker.model.IngredientAmountKey;
import com.george.tracker.model.IngredientStock;
import com.george.tracker.model.Recipe;
import com.george.tracker.repository.IngredientStockRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

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

    public void deleteIngredientStock(Long ingredientId, Long recipeId) {
        IngredientStock ingredientStockToBeDeleted = readIngredientStock(ingredientId, recipeId);
        ingredientStockRepository.delete(ingredientStockToBeDeleted);
    }

    public IngredientStock readIngredientStock(Long ingredientId, Long recipeId) {
        return ingredientStockRepository.findById(new IngredientAmountKey(ingredientId, recipeId))
                .orElseThrow(() -> new IngredientStockNotFoundException("Ingredient stock not found"));
    }

    public List<IngredientStock> readIngredientStocksOfRecipe(Long recipeId) {
        return ingredientStockRepository.findByRecipeId(recipeId);
    }

    public int getTotalCarbohydrates(Set<IngredientStock> ingredientStocks) {
        int carbohydrates = 0;
        for (IngredientStock is : ingredientStocks) {
            Ingredient ingredient = is.getIngredient();
            int quantity = is.getQuantity();
            carbohydrates += ((quantity * ingredient.getCarbohydrates()) / 100);
        }
        return carbohydrates;
    }

    public int getTotalProteins(Set<IngredientStock> ingredientStocks) {
        int proteins = 0;
        for (IngredientStock is : ingredientStocks) {
            Ingredient ingredient = is.getIngredient();
            int quantity = is.getQuantity();
            proteins += ((quantity * ingredient.getProteins()) / 100);
        }
        return proteins;
    }

    public int getTotalFats(Set<IngredientStock> ingredientStocks) {
        int fats = 0;
        for (IngredientStock is : ingredientStocks) {
            Ingredient ingredient = is.getIngredient();
            int quantity = is.getQuantity();
            fats += ((quantity * ingredient.getFats()) / 100);
        }
        return fats;
    }
}
