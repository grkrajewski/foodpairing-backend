package com.myapp.foodpairingbackend.exception;

public class IdException extends Exception {

    public static final String ID_FOUND = "Id found.";
    public static final String ID_NOT_FOUND = "Id not found.";

    public IdException(String message) {
        super(message);
    }
}
