package com.george.tracker.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PositiveOrZero;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "ingredient")
public class Ingredient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotBlank(message = "The ingredient should have a name")
    private String name;

    private String brand;

    /**
     * In the future quantity will be a request param used in ingredient service for determine the correct amount of
     * carbs, proteins,fats and so on ...
     * Each ingredient will be stored in db weighing 100g (first time the ingredient will be scraped from google search API)
     * and then every time when you introduce a ingredient in a meal, you will search for it in db firstly, and you will apply the
     * formula depending on quantity to find out carbs, proteins..
     * "In UI a message like "Search for ingredient or add it manual"
     */
//    @Positive(message = "The quantity should be greater than 0")
//    private int quantity;

    @PositiveOrZero(message = "The amount of carbohydrates should be positive")
    private int carbohydrates;

    @PositiveOrZero(message = "The amount of proteins should be positive")
    private int proteins;

    @PositiveOrZero(message = "The amount of fats should be positive")
    private int fats;

    @PositiveOrZero(message = "The amount of fibers should be positive")
    private int fibers;

    @OneToMany(mappedBy = "ingredient")
    private Set<IngredientStock> ingredientStocks;
}
