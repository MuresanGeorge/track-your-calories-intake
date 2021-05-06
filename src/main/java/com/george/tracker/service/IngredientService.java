package com.george.tracker.service;

import com.george.tracker.exception.IngredientNotFoundException;
import com.george.tracker.model.Ingredient;
import com.george.tracker.model.Macronutrient;
import com.george.tracker.repository.IngredientRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class IngredientService {

    private final IngredientRepository ingredientRepository;


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

    public int getTotalNumberOfCalories(List<Ingredient> ingredients) {
        int totalNumberOfCalories = 0;

        for (Ingredient i : ingredients) {
            int caloriesFromFats = Integer.parseInt(i.getFats()) * Integer.parseInt(Macronutrient.FAT.getValue());
            int caloriesFromCarbohydrates = Integer.parseInt(i.getCarbohydrates()) * Integer.parseInt(Macronutrient.CARBOHYDRATE.getValue());
            int caloriesFromProteins = Integer.parseInt(i.getProteins()) * Integer.parseInt(Macronutrient.PROTEIN.getValue());
            totalNumberOfCalories += caloriesFromFats + caloriesFromCarbohydrates + caloriesFromProteins;
        }
        return totalNumberOfCalories;
    }
}
