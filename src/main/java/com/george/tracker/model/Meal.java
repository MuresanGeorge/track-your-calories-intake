package com.george.tracker.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "meal")
public class Meal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotBlank(message = "The meal should have a name")
    private String name;

    @OneToMany(
            mappedBy = "meal",
            cascade = {CascadeType.PERSIST, CascadeType.MERGE},
            orphanRemoval = true
    )
    private Set<IngredientStore> ingredientsStore = new HashSet<>();

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @ManyToMany(mappedBy = "meals")
    private Set<Consumption> consumptions = new HashSet<>();

    public void addIngredientStore(IngredientStore ingredientStore) {
        ingredientsStore.add(ingredientStore);
        ingredientStore.setMeal(this);
    }

    public void removeIngredientStore(IngredientStore ingredientStore) {
        ingredientsStore.remove(ingredientStore);
        ingredientStore.setMeal(null);
    }

}

