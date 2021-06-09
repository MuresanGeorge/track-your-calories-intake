package com.george.tracker.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
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
@Builder
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

    private int calories;

    @OneToMany(mappedBy = "recipe", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private Set<IngredientStock> ingredientStocks = new HashSet<>();

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @ManyToMany(mappedBy = "recipes")
    private Set<Consumption> consumptions = new HashSet<>();

    private boolean isUpdated;

    private int weight;
}
