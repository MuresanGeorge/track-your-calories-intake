package com.george.tracker.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.george.tracker.model.Ingredient;
import com.george.tracker.service.IngredientService;
import com.george.tracker.transport.IngredientDto;
import com.george.tracker.transport.IngredientResponse;
import com.george.tracker.transport.usda.FoodUsda;
import com.george.tracker.util.MapUtil;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/ingredients")
public class IngredientController {

    private final IngredientService ingredientService;

    private final MapUtil mapUtil;


    public IngredientController(IngredientService ingredientService, MapUtil mapUtil) {
        this.ingredientService = ingredientService;
        this.mapUtil = mapUtil;
    }


    @ApiOperation(value = "Create a new ingredient", notes = "Returns 201 Created if the ingredient is successfully created")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Success"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 401, message = "Not authenticated"),
            @ApiResponse(code = 404, message = "Data not found"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public void createIngredient(@Valid @RequestBody IngredientDto ingredientDto) {
        Ingredient ingredient = mapUtil.mapToIngredient(ingredientDto);
        ingredientService.create(ingredient);
    }


    @ApiOperation(value = "Retrieve the ingredients according to the params ")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = IngredientResponse.class),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 401, message = "Not authenticated"),
            @ApiResponse(code = 404, message = "Data not found"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    public IngredientResponse readIngredients(@RequestParam(required = false) String name,
                                              @RequestParam(required = false) String brand) {
        List<Ingredient> ingredients = ingredientService.readIngredients(name, brand);
        List<IngredientDto> ingredientsDto = mapUtil.mapToIngredientDtoList(ingredients);
        return new IngredientResponse(ingredientsDto);
    }


    @ApiOperation(value = "Retrieve the ingredients/foods from USDA API especially vegetables and fruits ")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = IngredientResponse.class),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 401, message = "Not authenticated"),
            @ApiResponse(code = 404, message = "Data not found"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/usda/{foodName}")
    public IngredientResponse readFood(@PathVariable String foodName) {
        List<FoodUsda> usdaIngredients;
        List<Ingredient> ingredients = new ArrayList<>();
        try {
            usdaIngredients = ingredientService.readFoodFromUsda(foodName);
            ingredients = mapUtil.mapToIngredientList(usdaIngredients);
        } catch (JsonProcessingException e) {
            log.error(e.getMessage());
        }
        List<IngredientDto> ingredientsDto = mapUtil.mapToIngredientDtoList(ingredients);

        return new IngredientResponse(ingredientsDto);
    }


    @ApiOperation(value = "Update a ingredient", notes = "Returns 200 OK if the ingredient is successfully updated")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 401, message = "Not authenticated"),
            @ApiResponse(code = 404, message = "Data not found"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    @ResponseStatus(HttpStatus.OK)
    @PutMapping("/{id}")
    public void updateIngredient(@Valid @RequestBody IngredientDto ingredientDto, @PathVariable Long id) {
        Ingredient ingredient = mapUtil.mapToIngredient(ingredientDto);
        ingredientService.updateIngredient(id, ingredient);
    }
}
