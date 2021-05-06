package com.george.tracker.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "ingredient")
public class Ingredient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotBlank(message = "The ingredient should have a name")
    private String name;

    /**
     * In the future quantity will be a request param used in ingredient service for determine the correct amount of
     * carbs, proteins,fats and so on ...
     * Each ingredient will be stored in db weighing 100g (first time the ingredient will be scraped from google search API)
     */
    @Positive(message = "The quantity should be greater than 0")
    private int quantity;

    @ManyToOne(fetch = FetchType.LAZY)
    private Recipe recipe;

    @ManyToOne(fetch = FetchType.LAZY)
    private Meal meal;

    private String carbohydrates;

    private String proteins;

    private String fats;

    private String fibers;
}
