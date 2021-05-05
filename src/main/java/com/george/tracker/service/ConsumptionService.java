package com.george.tracker.service;

import com.george.tracker.model.Consumption;
import com.george.tracker.model.Meal;
import com.george.tracker.model.Recipe;
import com.george.tracker.repository.ConsumptionRepository;
import org.springframework.stereotype.Service;

@Service
public class ConsumptionService {

    private ConsumptionRepository consumptionRepository;

    public ConsumptionService (ConsumptionRepository consumptionRepository) {
        this.consumptionRepository = consumptionRepository;
    }

    public void create(int dailyIntake, Meal meal, Recipe recipe) {
        //insert the business logic here
//        consumptionRepository.save(consumption);
    }
}
