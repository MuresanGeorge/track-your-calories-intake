package com.george.tracker.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Recipe {

    private long id;

    private String description;

    private List<Ingredient> ingredients;

    private int calories;
}
