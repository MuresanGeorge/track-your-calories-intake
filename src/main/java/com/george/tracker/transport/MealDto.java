package com.george.tracker.transport;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MealDto {

    @NotBlank(message = "The meal should have a name")
    private String name;

    private Map<Long, Long> ingredientsQuantities;
}
