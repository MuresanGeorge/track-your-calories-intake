package com.george.tracker.controller;

import com.george.tracker.model.Recipe;
import com.george.tracker.service.RecipeService;
import com.george.tracker.transport.recipe.RecipeDto;
import com.george.tracker.transport.recipe.RecipeResponse;
import com.george.tracker.util.MapUtil;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
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
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/recipes")
public class RecipeController {

    private final RecipeService recipeService;

    private final MapUtil mapUtil;

    public RecipeController(RecipeService recipeService, MapUtil mapUtil) {
        this.recipeService = recipeService;
        this.mapUtil = mapUtil;
    }


    @ApiOperation(value = "Create a new recipe", notes = "Returns 201 Created if the recipe is successfully created")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Success"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 401, message = "Not authenticated"),
            @ApiResponse(code = 404, message = "Data not found"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public void createRecipe(@Valid @RequestBody RecipeDto recipeDto) {
        String recipeName = recipeDto.getName();
        String recipeDescription = recipeDto.getDescription();
        Map<Long, Long> ingredientsQuantities = recipeDto.getIngredientQuantity();

        recipeService.create(recipeName, recipeDescription, ingredientsQuantities);
    }


    @ApiOperation(value = "Update a recipe", notes = "Returns 200 Success if the recipe is successfully updated")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 401, message = "Not authenticated"),
            @ApiResponse(code = 404, message = "Data not found"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    @ResponseStatus(HttpStatus.OK)
    @PutMapping("/{id}")
    public void updateRecipe(@Valid @RequestBody RecipeDto recipeDto, @PathVariable Long id) {
        String recipeName = recipeDto.getName();
        String recipeDescription = recipeDto.getDescription();
        Map<Long, Long> ingredientsQuantities = recipeDto.getIngredientQuantity();

        recipeService.updateRecipe(id, recipeName, recipeDescription, ingredientsQuantities);
    }


    @ApiOperation(value = "Update the calories in a recipe", notes = "Returns 200 Success if the recipe is successfully updated")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = Integer.class),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 401, message = "Not authenticated"),
            @ApiResponse(code = 404, message = "Data not found"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    @ResponseStatus(HttpStatus.OK)
    @PutMapping("/calories/{id}")
    public Integer updateCaloriesInARecipe(@RequestParam Integer weight, @PathVariable Long id) {
        return recipeService.updateCalories(weight, id);
    }


    @ApiOperation(value = "Retrieve the recipes/recipe according to the params ")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = RecipeResponse.class),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 401, message = "Not authenticated"),
            @ApiResponse(code = 404, message = "Data not found"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    public RecipeResponse readRecipes(@RequestParam(required = false) Long id,
                                      @RequestParam(required = false) String name,
                                      @RequestParam(required = false) String ingredientName,
                                      @RequestParam(required = false) List<Long> ingredients) {
        List<Recipe> recipes = recipeService.readRecipes(id, name, ingredientName, ingredients);
        List<RecipeDto> recipesDto = mapUtil.mapToRecipesDto(recipes);
        return new RecipeResponse(recipesDto);
    }
}
