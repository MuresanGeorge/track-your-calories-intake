package com.george.tracker.util;

import com.george.tracker.model.Ingredient;
import com.george.tracker.transport.IngredientDto;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class MapUtil {

    private final ModelMapper modelMapper;

    public MapUtil(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public List<IngredientDto> mapIngredientList(List<Ingredient> ingredients) {
        return ingredients
                .stream()
                .map(ingredient -> modelMapper.map(ingredient, IngredientDto.class))
                .collect(Collectors.toList());
    }

    public IngredientDto mapIngredient(Ingredient ingredient) {
        return modelMapper.map(ingredient, IngredientDto.class);
    }

    public Ingredient mapIngredientDto(IngredientDto ingredientDto) {
        return modelMapper.map(ingredientDto, Ingredient.class);
    }

}
