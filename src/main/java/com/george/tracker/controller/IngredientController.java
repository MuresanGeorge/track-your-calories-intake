package com.george.tracker.controller;

import com.george.tracker.model.Ingredient;
import com.george.tracker.service.IngredientService;
import com.george.tracker.transport.IngredientDto;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
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
import org.springframework.web.reactive.function.client.WebClient;

import javax.validation.Valid;

@RestController
@RequestMapping("/ingredients")
public class IngredientController {

    private final IngredientService ingredientService;

    private final ModelMapper mapper;

    @Value("${api.key}")
    private String usdaKey;

    public IngredientController(IngredientService ingredientService, ModelMapper modelMapper) {
        this.ingredientService = ingredientService;
        this.mapper = modelMapper;
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
        Ingredient ingredient = mapper.map(ingredientDto, Ingredient.class);
        ingredientService.create(ingredient);
    }


    @ApiOperation(value = "Retrieve the ingredients according to the params ")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = IngredientDto.class),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 401, message = "Not authenticated"),
            @ApiResponse(code = 404, message = "Data not found"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    public IngredientDto readIngredients(@RequestParam(required = false) String name,
                                        @RequestParam(required = false) String brand) {
        Ingredient ingredient = ingredientService.readIngredient(name, brand);
        return mapper.map(ingredient, IngredientDto.class);
    }


    @GetMapping("/food/{foodName}")
    public String getFood(@PathVariable String foodName) {
        return WebClient.create()
                .get()
                .uri("https://api.nal.usda.gov/fdc/v1/foods/search?query=" + foodName + "&api_key=" + usdaKey)
                .retrieve()
                .bodyToMono(String.class)
                .block();
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
        Ingredient ingredient = mapper.map(ingredientDto, Ingredient.class);
        ingredientService.updateIngredient(id, ingredient);
    }
}
