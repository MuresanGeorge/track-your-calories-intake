package com.george.tracker.model;

import com.sun.xml.bind.v2.TODO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "recipe")
public class Recipe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotBlank(message = "The recipe should have a name")
    private String name;

    @NotBlank(message = "The recipe should contains steps to follow")
    private String description;

    @OneToMany(
            mappedBy = "recipe",
            cascade = CascadeType.ALL)
    private List<Ingredient> ingredients;

    @ManyToOne(fetch = FetchType.LAZY)
    private Consumption consumption;

    private String calories;

    //TODO: move this logic in a specific service
    private String getTotalNumberOfCalories() {
        int totalNumberOfCalories = 0;

        for (Ingredient i : this.ingredients) {
            int caloriesFromFats = Integer.parseInt(i.getFats()) * Integer.parseInt(Macronutrient.FAT.getValue());
            int caloriesFromCarbohydrates = Integer.parseInt(i.getCarbohydrates()) * Integer.parseInt(Macronutrient.CARBOHYDRATE.getValue());
            int caloriesFromProteins = Integer.parseInt(i.getProteins()) * Integer.parseInt(Macronutrient.PROTEIN.getValue());
            totalNumberOfCalories += caloriesFromFats + caloriesFromCarbohydrates + caloriesFromProteins;
        }
        return String.valueOf(totalNumberOfCalories);
    }
}
