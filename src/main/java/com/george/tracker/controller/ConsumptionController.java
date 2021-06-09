package com.george.tracker.controller;

import com.george.tracker.model.Meal;
import com.george.tracker.service.ConsumptionService;
import com.george.tracker.transport.consumption.ConsumptionDto;
import com.george.tracker.util.MapUtil;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/intakes")
public class ConsumptionController {

    private final ConsumptionService consumptionService;

    private final MapUtil mapUtil;

    public ConsumptionController(ConsumptionService consumptionService, MapUtil mapUtil) {
        this.consumptionService = consumptionService;
        this.mapUtil = mapUtil;
    }


    @ApiOperation(value = "Create a new daily intake", notes = "Returns 201 Created if the daily intake is successfully created")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Success"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 401, message = "Not authenticated"),
            @ApiResponse(code = 404, message = "Data not found"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public void createConsumption(@Valid @RequestBody ConsumptionDto consumptionDto) {
        int dailyIntake = consumptionDto.getDesiredDailyIntake();
        List<Meal> meals = mapUtil.mapToMeals(consumptionDto.getMeals());
        Map<Long, Long> recipesAndQuantities = consumptionDto.getRecipesWithQuantities();

        consumptionService.create(dailyIntake, meals, recipesAndQuantities);
    }


    @ApiOperation(value = "Update the daily intake", notes = "Returns 200 OK if the daily intake is successfully updated")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 401, message = "Not authenticated"),
            @ApiResponse(code = 404, message = "Data not found"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    @ResponseStatus(HttpStatus.OK)
    @PutMapping("/{id}")
    public void updateConsumption(@Valid @RequestBody ConsumptionDto consumptionDto, @PathVariable long id) {
        int dailyIntake = consumptionDto.getDesiredDailyIntake();
        List<Meal> meals = mapUtil.mapToMeals(consumptionDto.getMeals());
        Map<Long, Long> recipesAndQuantities = consumptionDto.getRecipesWithQuantities();

        consumptionService.updateConsumption(dailyIntake, meals, recipesAndQuantities, id);
    }
}
