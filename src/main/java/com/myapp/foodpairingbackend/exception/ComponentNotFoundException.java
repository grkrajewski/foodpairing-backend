package com.myapp.foodpairingbackend.exception;

public class ComponentNotFoundException extends Exception {

    public static final String DISH = "Dish with given id doesn't exist.";
    public static final String DRINK = "Drink with given id doesn't exist.";
    public static final String DRINK_INGREDIENT = "Drink ingredient with given id doesn't exist.";
    public static final String COMPOSITION = "Composition with given id doesn't exist.";
    public static final String COMMENT = "Comment with given id doesn't exist.";
    public static final String REACTION = "Reaction with given id doesn't exist.";

    public ComponentNotFoundException(String message) {
        super(message);
    }
}
