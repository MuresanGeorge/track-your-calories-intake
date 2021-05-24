package com.george.tracker.transport.usda;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FoodUsdaResponse {

    private List<FoodUsda> foods;
}
