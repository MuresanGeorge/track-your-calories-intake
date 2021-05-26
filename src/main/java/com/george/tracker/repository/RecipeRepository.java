package com.george.tracker.repository;

import com.george.tracker.model.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface RecipeRepository extends JpaRepository<Recipe, Long> {

    List<Recipe> findByNameContainingIgnoreCase(String name);

    List<Recipe> findByIngredientStocks_IngredientNameContains(String ingredientName);

    Set<Recipe> findByIngredientStocks_IngredientIdIn(List<Long> ingredientIds);
}
