package com.george.tracker.service;

import com.george.tracker.exception.IngredientNotFoundException;
import com.george.tracker.model.Ingredient;
import com.george.tracker.repository.IngredientRepository;
import org.springframework.stereotype.Service;

@Service
public class IngredientService {

    private IngredientRepository ingredientRepository;


    public IngredientService(IngredientRepository ingredientRepository) {
        this.ingredientRepository = ingredientRepository;
    }

    public void create(Ingredient ingredient) {
        ingredientRepository.save(ingredient);
    }

    public Ingredient readIngredient(String name) {
        return ingredientRepository.findByName(name)
                .orElseThrow(() -> new IngredientNotFoundException("Ingredient with name " + name + " not found"));
    }
}
