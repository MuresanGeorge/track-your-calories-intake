package com.george.tracker.exception;

public class RecipeNotUpdatedException extends RuntimeException {
    public RecipeNotUpdatedException(String message) {
        super(message);
    }
}
