package com.george.tracker.exception;

public class IngredientStockNotFoundException extends DataNotFoundException {
    public IngredientStockNotFoundException(String message) {
        super(message);
    }
}
