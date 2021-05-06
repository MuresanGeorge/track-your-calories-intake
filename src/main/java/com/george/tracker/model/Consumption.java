package com.george.tracker.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.Positive;
import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "consumption")
public class Consumption {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Positive(message = "The daily intake should be greater than 0")
    private int desiredDailyIntake;

    @FutureOrPresent(message = "The consumption date should be today or in the future")
    private LocalDate timestamp;

    @OneToMany(
            mappedBy = "consumption",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    private List<Meal> meals;

    @OneToMany(
            mappedBy = "consumption",
            cascade = CascadeType.ALL)
    private List<Recipe> recipes;
}