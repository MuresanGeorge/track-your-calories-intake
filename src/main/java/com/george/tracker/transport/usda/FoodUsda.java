package com.george.tracker.transport.usda;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FoodUsda {

    private String lowercaseDescription;

    private List<FoodNutrientUsda> foodNutrients;

    private List<FoodNutrientUsda> getRelevantNutrients() {
        return this.foodNutrients.stream()
                .filter(nutrient -> nutrient.getNutrientNumber().equals(MacronutrientUsda.PROTEINUSDA.getValue())
                        || nutrient.getNutrientNumber().equals(MacronutrientUsda.FATUSDA.getValue())
                        || nutrient.getNutrientNumber().equals(MacronutrientUsda.CARBOHYDRATEUSDA.getValue())
                        || nutrient.getNutrientNumber().equals(MacronutrientUsda.FIBERUSDA.getValue()))
                .collect(Collectors.toList());
    }

    public FoodNutrientUsda getSpecificNutrient(String nutrientNumber) {
        FoodNutrientUsda nutrient = new FoodNutrientUsda();
        List<FoodNutrientUsda> relevantNutrients = getRelevantNutrients();
        for (FoodNutrientUsda foodNutrient : relevantNutrients) {
            if (foodNutrient.getNutrientNumber().equals(nutrientNumber)) {
                nutrient = foodNutrient;
            }
        }
        return nutrient;
    }
}
