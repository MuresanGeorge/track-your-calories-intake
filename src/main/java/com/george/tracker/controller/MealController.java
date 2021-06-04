package com.george.tracker.controller;

import com.george.tracker.service.MealService;
import com.george.tracker.transport.MealDto;
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
import java.util.Map;

@RestController
@RequestMapping("/meals")
public class MealController {

    private final MealService mealService;

    private final MapUtil mapUtil;

    public MealController(MealService mealService, MapUtil mapUtil) {
        this.mealService = mealService;
        this.mapUtil = mapUtil;
    }


    @ApiOperation(value = "Create a new meal", notes = "Returns 201 Created if the meal is successfully created")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Success"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 401, message = "Not authenticated"),
            @ApiResponse(code = 404, message = "Data not found"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public void createMeal(@Valid @RequestBody MealDto mealDto) {
        String mealName = mealDto.getName();
        Map<Long, Long> ingredientsAmount = mealDto.getIngredientsQuantities();

        mealService.create(mealName, ingredientsAmount);
    }


    @ApiOperation(value = "Update a meal", notes = "Returns 200 OK if the meal is successfully updated")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 401, message = "Not authenticated"),
            @ApiResponse(code = 404, message = "Data not found"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    @ResponseStatus(HttpStatus.OK)
    @PutMapping("/{id}")
    public void updateMeal(@Valid @RequestBody MealDto mealDto, @PathVariable Long id) {
        String newMealName = mealDto.getName();
        Map<Long, Long> newIngredientsAmount = mealDto.getIngredientsQuantities();

        mealService.updateMeal(id, newMealName, newIngredientsAmount);
    }

}
