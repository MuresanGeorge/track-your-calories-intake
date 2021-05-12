package com.george.tracker.service;

import com.george.tracker.model.Ingredient;
import com.george.tracker.model.Recipe;
import com.george.tracker.repository.RecipeRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RecipeService {

    private final RecipeRepository recipeRepository;

    private final IngredientService ingredientService;

    public RecipeService(RecipeRepository recipeRepository, IngredientService ingredientService) {
        this.recipeRepository = recipeRepository;
        this.ingredientService = ingredientService;
    }

    public void create(String name, String description, List<Ingredient> ingredients) {
        Recipe recipe = new Recipe();
        recipe.setCalories(ingredientService.getTotalNumberOfCalories(ingredients));
        recipe.setName(name);
        recipe.setDescription(description);

//        if (!ingredients.isEmpty()) {
//            for (Ingredient i : ingredients) {
//                recipe.addIngredient(i);
//                ingredientService.create(i);
//            }
//        }
//        recipe.setIngredients(ingredients);
        recipeRepository.save(recipe);
    }

//    public int getTotalNumberOfCaloriesInRecipe(Recipe recipe) {
//        return ingredientService.getTotalNumberOfCalories(recipe.getIngredients());
//    }
}
