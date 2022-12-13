package com.myapp.foodpairingbackend.controller;

import com.myapp.foodpairingbackend.domain.dto.DishDto;
import com.myapp.foodpairingbackend.exception.*;
import com.myapp.foodpairingbackend.facade.DishFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/foodpairing/v1/dishes")
@RequiredArgsConstructor
public class DishController {

    private final DishFacade dishFacade;

    @GetMapping
    public ResponseEntity<List<DishDto>> getDishes() {
        return ResponseEntity.ok(dishFacade.getDishes());
    }

    @GetMapping(value = "{dishId}")
    public ResponseEntity<DishDto> getDish(@PathVariable Long dishId) throws DishNotFoundException {
        return ResponseEntity.ok(dishFacade.getDish(dishId));
    }

    @DeleteMapping(value = "{dishId}")
    public ResponseEntity<Void> deleteDish(@PathVariable Long dishId) {
        dishFacade.deleteDish(dishId);
        return ResponseEntity.ok().build();
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DishDto> saveDishInDb(@RequestBody DishDto dishDto) throws DrinkNotFoundException,
            DishNotFoundException, CompositionNotFoundException, CommentNotFoundException, IdFoundException {
        return ResponseEntity.ok(dishFacade.saveDishInDb(dishDto));
    }

    @PutMapping
    public ResponseEntity<DishDto> updateDish(@RequestBody DishDto dishDto) throws DrinkNotFoundException,
            DishNotFoundException, CompositionNotFoundException, CommentNotFoundException, IdNotFoundException {
        return ResponseEntity.ok(dishFacade.updateDish(dishDto));
    }
}
