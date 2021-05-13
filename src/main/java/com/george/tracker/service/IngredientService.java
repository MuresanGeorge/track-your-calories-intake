package com.george.tracker.service;

import com.george.tracker.exception.IngredientDuplicateException;
import com.george.tracker.exception.IngredientNotFoundException;
import com.george.tracker.model.Ingredient;
import com.george.tracker.model.Macronutrient;
import com.george.tracker.repository.IngredientRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class IngredientService {

    private final IngredientRepository ingredientRepository;


    public IngredientService(IngredientRepository ingredientRepository) {
        this.ingredientRepository = ingredientRepository;
    }

    public Ingredient create(Ingredient ingredient) {
        checkIfIngredientExists(ingredient.getName(), ingredient.getBrand());
        return ingredientRepository.save(ingredient);
    }

    public Ingredient readIngredient(String name, String brand) {
        Ingredient ingredient = new Ingredient();
        return ingredientRepository.findByName(name)
                .orElseThrow(() -> new IngredientNotFoundException("Ingredient with name " + name + " not found"));
    }

    public void updateIngredient(long ingredientId, Ingredient ingredientRequest) {
        Ingredient ingredientToBeUpdated = ingredientRepository.findById(ingredientId)
                .orElseThrow(() -> new IngredientNotFoundException("Ingredient with id " + ingredientId + "not found"));

        setNewProperties(ingredientRequest, ingredientToBeUpdated);
        ingredientRepository.save(ingredientToBeUpdated);
    }

    public int getTotalNumberOfCalories(List<Ingredient> ingredients) {
        int totalNumberOfCalories = 0;

        if (!ingredients.isEmpty()) {
            for (Ingredient i : ingredients) {
                int caloriesFromFats = (i.getFats()) * Integer.parseInt(Macronutrient.FAT.getValue());
                int caloriesFromCarbohydrates = (i.getCarbohydrates()) * Integer.parseInt(Macronutrient.CARBOHYDRATE.getValue());
                int caloriesFromProteins = (i.getProteins()) * Integer.parseInt(Macronutrient.PROTEIN.getValue());
                totalNumberOfCalories += caloriesFromFats + caloriesFromCarbohydrates + caloriesFromProteins;
            }
        }
        return totalNumberOfCalories;
    }

    private void checkIfIngredientExists(String name, String brand) {
        Optional<Ingredient> foundIngredient = ingredientRepository.findByNameAndBrand(name, brand);
        if (foundIngredient.isPresent()) {
            throw new IngredientDuplicateException("Ingredient with name " + name + " from the brand " + brand +
                    " already exists");
        }
    }

    private void setNewProperties(Ingredient ingredientRequest, Ingredient ingredientToBeUpdated) {
        ingredientToBeUpdated.setName(ingredientRequest.getName());
        ingredientToBeUpdated.setBrand(ingredientRequest.getBrand());
        ingredientToBeUpdated.setCarbohydrates(ingredientRequest.getCarbohydrates());
        ingredientToBeUpdated.setFibers(ingredientRequest.getFibers());
        ingredientToBeUpdated.setFats(ingredientRequest.getFats());
        ingredientToBeUpdated.setProteins(ingredientRequest.getProteins());
    }
}
