package com.george.tracker.transport.usda;

public enum MacronutrientUsda {

    FATUSDA("204"), PROTEINUSDA("203"), CARBOHYDRATEUSDA("205"), FIBERUSDA("291");

    private String value;

    MacronutrientUsda(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
