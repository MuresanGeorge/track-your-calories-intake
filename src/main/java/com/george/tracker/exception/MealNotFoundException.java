package com.george.tracker.exception;

public class MealNotFoundException extends DataNotFoundException {
    public MealNotFoundException(String message) {
        super(message);
    }
}
