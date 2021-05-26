package com.george.tracker.exception;

public class RecipeNotFoundException extends DataNotFoundException {
    public RecipeNotFoundException(String message) {
        super(message);
    }
}
