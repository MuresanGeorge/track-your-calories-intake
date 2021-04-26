package com.george.tracker.model;

public enum Macronutrient {
    FAT("9"), PROTEIN("4"), CARBOHYDRATE("4"), FIBER("0");

    private String value;

    Macronutrient(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
