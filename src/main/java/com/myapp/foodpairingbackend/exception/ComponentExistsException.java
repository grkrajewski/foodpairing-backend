package com.myapp.foodpairingbackend.exception;

public class ComponentExistsException extends Exception {

    public static final String DRINK_EXISTS = "Drink with given id is already paired with other composition - choose a different one.";
    public static final String DISH_EXISTS = "Dish with given external system id already exists.";

    public ComponentExistsException(String message) {
        super(message);
    }
}
