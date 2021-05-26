package com.george.tracker.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.george.tracker.exception.IngredientDuplicateException;
import com.george.tracker.exception.IngredientNotFoundException;
import com.george.tracker.model.Ingredient;
import com.george.tracker.model.Macronutrient;
import com.george.tracker.repository.IngredientRepository;
import com.george.tracker.transport.usda.FoodUsda;
import com.george.tracker.transport.usda.FoodUsdaResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class IngredientService {

    private final IngredientRepository ingredientRepository;

    @Value("${usda.base.url}")
    private String usdaBaseUrl;

    @Value("${usda.api.key}")
    private String usdaKey;


    public IngredientService(IngredientRepository ingredientRepository) {
        this.ingredientRepository = ingredientRepository;
    }


    public Ingredient create(Ingredient ingredient) {
        checkIfIngredientExists(ingredient.getName(), ingredient.getBrand());
        return ingredientRepository.save(ingredient);
    }

    public List<Ingredient> readIngredients(String name, String brand) {
        List<Ingredient> allIngredients = ingredientRepository.findAll();

        allIngredients = filterByName(name, allIngredients);
        allIngredients = filterByBrand(brand, allIngredients);
        allIngredients = filterByNameAndBrand(name, brand, allIngredients);

        return allIngredients;
    }

    public Ingredient readIngredient(Long ingredientId) {
        return ingredientRepository.findById(ingredientId)
                .orElseThrow(() -> new IngredientNotFoundException("Ingredient with id " + ingredientId + "not found"));
    }

    public List<FoodUsda> readFoodFromUsda(String foodName) throws JsonProcessingException {
        //TODO put these params within an enum and come back here and resolve this commented lines !!!!!
        /*
        String response = WebClient.create()
                .get()
                .uri(uriBuilder -> uriBuilder.path(usdaBaseUrl)
                        .queryParam("query", foodName)
                        .queryParam("dataType", String.join(",", "Foundation", "SR Legacy"))
                        .queryParam("pageNumber", 1)
                        .queryParam("pageSize", 3)
                        .queryParam("api_key", usdaKey)
                        .build())
                .retrieve()
                .bodyToMono(String.class)
                .log()
                .block();
         */
        String response = WebClient.create()
                .get()
                .uri(usdaBaseUrl + "?query=" + foodName + "&api_key=" + usdaKey
                        + "&dataType=SR Legacy,Foundation&pageSize=3&pageNumber=1")
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(String.class)
                .log()
                .block();

        ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        FoodUsdaResponse foodUsdaResponse = mapper.readValue(response, FoodUsdaResponse.class);

        return foodUsdaResponse.getFoods();
    }

    public void updateIngredient(long ingredientId, Ingredient ingredientRequest) {
        Ingredient ingredientToBeUpdated = readIngredient(ingredientId);
        setNewProperties(ingredientRequest, ingredientToBeUpdated);

        ingredientRepository.save(ingredientToBeUpdated);
    }

    /**
     * This method is used to calculate the total number of calories per 100g of recipe based on all the ingredients and
     * the calories in the ingredients
     *
     * @param ingredientsQuantities the map with ingredient id and ingredient quantity
     * @return the number of calories in 100g of recipe
     */
    public int calculateTotalCalories(Map<Long, Long> ingredientsQuantities) {
        int calories = 0;
        int totalAmountOfFood = 0;

        for (Map.Entry<Long, Long> pair : ingredientsQuantities.entrySet()) {
            Ingredient ingredient = readIngredient(pair.getKey());
            calories += (pair.getValue() * getNumberOfCaloriesInIngredient(ingredient)) / 100;
            totalAmountOfFood += pair.getValue();
        }
        return calories * 100 / totalAmountOfFood;
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

    private List<Ingredient> filterByBrand(String brand, List<Ingredient> ingredients) {
        if (brand == null) {
            return ingredients;
        }
        return ingredients.stream()
                .filter(ingredient -> ingredient.getBrand().equals(brand))
                .collect(Collectors.toList());
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

    /**
     * This method is used to calculate the total number of calories per 100g of ingredient depending on the macros
     * (fats, proteins and carbohydrates)
     *
     * @param ingredient the ingredient whose calories we calculate
     * @return the number of calories of ingredient
     */
    private int getNumberOfCaloriesInIngredient(Ingredient ingredient) {
        int caloriesFromFats = (ingredient.getFats()) * Integer.parseInt(Macronutrient.FAT.getValue());
        int caloriesFromCarbohydrates = (ingredient.getCarbohydrates()) * Integer.parseInt(Macronutrient.CARBOHYDRATE.getValue());
        int caloriesFromProteins = (ingredient.getProteins()) * Integer.parseInt(Macronutrient.PROTEIN.getValue());

        return caloriesFromFats + caloriesFromCarbohydrates + caloriesFromProteins;
    }
}
