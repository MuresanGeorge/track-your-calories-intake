package com.george.tracker.util;

import com.george.tracker.model.Ingredient;
import com.george.tracker.transport.IngredientDto;
import com.george.tracker.transport.usda.FoodNutrientUsda;
import com.george.tracker.transport.usda.FoodUsda;
import com.george.tracker.transport.usda.MacronutrientUsda;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class MapUtil {

    private final ModelMapper modelMapper;

    public MapUtil(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public List<IngredientDto> mapToIngredientDtoList(List<Ingredient> ingredients) {
        return ingredients
                .stream()
                .map(ingredient -> modelMapper.map(ingredient, IngredientDto.class))
                .collect(Collectors.toList());
    }

    public List<Ingredient> mapToIngredientList(List<FoodUsda> foodsUsda) {
        List<Ingredient> ingredients = new ArrayList<>();
        foodsUsda.forEach(foodUsda -> {
            Ingredient i = mapFoodUsdaToIngredient(foodUsda);
            ingredients.add(i);
        });
        return ingredients;
    }

    public IngredientDto mapToIngredientDto(Ingredient ingredient) {
        return modelMapper.map(ingredient, IngredientDto.class);
    }

    public Ingredient mapToIngredient(IngredientDto ingredientDto) {
        return modelMapper.map(ingredientDto, Ingredient.class);
    }

    public Ingredient mapToIngredient(FoodUsda foodUsda) {
        modelMapper.typeMap(FoodUsda.class, Ingredient.class)
                .addMapping(FoodUsda::getLowercaseDescription, Ingredient::setName);
        return modelMapper.map(foodUsda, Ingredient.class);

    }

    public Ingredient mapFoodUsdaToIngredient(FoodUsda foodUsda) {
        Ingredient newIngredient = mapToIngredient(foodUsda);

        FoodNutrientUsda carbohydrates = foodUsda.getSpecificNutrient(MacronutrientUsda.CARBOHYDRATEUSDA.getValue());
        newIngredient.setCarbohydrates(carbohydrates.getValue());
        FoodNutrientUsda proteins = foodUsda.getSpecificNutrient(MacronutrientUsda.PROTEINUSDA.getValue());
        newIngredient.setProteins(proteins.getValue());
        FoodNutrientUsda fibers = foodUsda.getSpecificNutrient(MacronutrientUsda.FIBERUSDA.getValue());
        newIngredient.setFibers(fibers.getValue());
        FoodNutrientUsda fats = foodUsda.getSpecificNutrient(MacronutrientUsda.FATUSDA.getValue());
        newIngredient.setFats(fats.getValue());

        return newIngredient;
    }
}
