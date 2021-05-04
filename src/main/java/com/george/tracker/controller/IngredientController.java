package com.george.tracker.controller;

import com.george.tracker.model.Ingredient;
import com.george.tracker.service.IngredientService;
import com.george.tracker.transport.IngredientDto;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/ingredients")
public class IngredientController {

    private final IngredientService ingredientService;

    private final ModelMapper mapper;

    public IngredientController(IngredientService ingredientService, ModelMapper modelMapper) {
        this.ingredientService = ingredientService;
        this.mapper = modelMapper;
    }


    @PostMapping
    @ApiOperation(value = "Create a new ingredient", notes = "Returns 201 Created if the ingredient is successfully created")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Success"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 401, message = "Not authenticated"),
            @ApiResponse(code = 404, message = "Data not found"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    @ResponseStatus(HttpStatus.CREATED)
    public void createIngredient(@Valid @RequestBody IngredientDto ingredientDto) {
        Ingredient ingredient = mapper.map(ingredientDto, Ingredient.class);
        ingredientService.create(ingredient);
    }


    @GetMapping("/{name}")
    @ApiOperation(value = "Retrieve the ingredient", notes = "Returns 200 OK if the ingredient is successfully retrieved")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = IngredientDto.class),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 401, message = "Not authenticated"),
            @ApiResponse(code = 404, message = "Data not found"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    @ResponseStatus(HttpStatus.OK)
    public IngredientDto readIngredient(@PathVariable String name) {
        Ingredient ingredient = ingredientService.readIngredient(name);
        return mapper.map(ingredient, IngredientDto.class);
    }
}