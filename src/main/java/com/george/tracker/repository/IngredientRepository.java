package com.george.tracker.repository;

import com.george.tracker.model.Ingredient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IngredientRepository extends JpaRepository<Ingredient, Long> {

    //TODO verify this
    Optional<Ingredient> findByNameAndBrand(String name, String brand);

    Optional<Ingredient> findByName(String name);

}
