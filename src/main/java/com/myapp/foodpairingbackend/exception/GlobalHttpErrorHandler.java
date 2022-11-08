package com.myapp.foodpairingbackend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class GlobalHttpErrorHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(DishNotFoundException.class)
    public ResponseEntity<Object> handleDishNotFoundException(DishNotFoundException exception) {
        return new ResponseEntity<>("Dish with given id doesn't exist", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DrinkNotFoundException.class)
    public ResponseEntity<Object> handleDrinkNotFoundException(DrinkNotFoundException exception) {
        return new ResponseEntity<>("Drink with given id doesn't exist", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DrinkIngredientNotFoundException.class)
    public ResponseEntity<Object> handleDrinkIngredientNotFoundException(DrinkIngredientNotFoundException exception) {
        return new ResponseEntity<>("Drink ingredient with given id doesn't exist", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(CompositionNotFoundException.class)
    public ResponseEntity<Object> handleCompositionNotFoundException(CompositionNotFoundException exception) {
        return new ResponseEntity<>("Composition with given id doesn't exist", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(CommentNotFoundException.class)
    public ResponseEntity<Object> handleCommentNotFoundException(CommentNotFoundException exception) {
        return new ResponseEntity<>("Comment with given id doesn't exist", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ReactionNotFoundException.class)
    public ResponseEntity<Object> handleReactionNotFoundException(ReactionNotFoundException exception) {
        return new ResponseEntity<>("Rating with given id doesn't exist", HttpStatus.BAD_REQUEST);
    }
}
