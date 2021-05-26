package com.george.tracker.repository;

import com.george.tracker.model.IngredientAmountKey;
import com.george.tracker.model.IngredientStock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IngredientStockRepository extends JpaRepository<IngredientStock, IngredientAmountKey> {

    List<IngredientStock> findByRecipeId(Long id);
}
