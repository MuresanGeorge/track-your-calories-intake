package com.george.tracker.service;

import com.george.tracker.exception.IngredientDuplicateException;
import com.george.tracker.exception.IngredientNotFoundException;
import com.george.tracker.model.Ingredient;
import com.george.tracker.model.Macronutrient;
import com.george.tracker.repository.IngredientRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
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

    public Ingredient readIngredient(Long ingredientId) {
        return ingredientRepository.findById(ingredientId)
                .orElseThrow(() -> new IngredientNotFoundException("Ingredient with id " + ingredientId + " not found"));
    }

    public List<Ingredient> readIngredients(String name, String brand) {
        List<Ingredient> allIngredients = ingredientRepository.findAll();

        allIngredients = filterByName(name, allIngredients);
        allIngredients = filterByNameAndBrand(name, brand, allIngredients);

        return allIngredients;
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

    private List<Ingredient> filterByName(String name, List<Ingredient> ingredients) {
        if (name == null) {
            return ingredients;
        }
        return ingredientRepository.findByName(name);
    }

    private List<Ingredient> filterByNameAndBrand(String name, String brand, List<Ingredient> ingredients) {
        if (name != null && brand != null) {
            List<Ingredient> ingredientList = new ArrayList<>();
            Optional<Ingredient> optionalIngredient = ingredientRepository.findByNameAndBrand(name, brand);
            if (optionalIngredient.isPresent()) {
                ingredientList = Collections.singletonList(optionalIngredient.get());
            }
            return ingredientList;
        }
        return ingredients;
    }
}
