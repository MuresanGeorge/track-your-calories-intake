package com.george.tracker.repository;

import com.george.tracker.model.IngredientStore;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IngredientStoreRepository extends JpaRepository<IngredientStore, Long> {

    Optional<IngredientStore> findByIngredientIdAndMealId(Long ingredientId, Long mealId);
}
