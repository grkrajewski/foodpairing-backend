package com.myapp.foodpairingbackend.controller;

import com.myapp.foodpairingbackend.domain.dto.DrinkIngredientDto;
import com.myapp.foodpairingbackend.exception.DrinkIngredientNotFoundException;
import com.myapp.foodpairingbackend.exception.DrinkNotFoundException;
import com.myapp.foodpairingbackend.exception.IdFoundException;
import com.myapp.foodpairingbackend.exception.IdNotFoundException;
import com.myapp.foodpairingbackend.facade.DrinkIngredientFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/foodpairing/v1/drinkingredients")
@RequiredArgsConstructor
public class DrinkIngredientController {

    private final DrinkIngredientFacade drinkIngredientFacade;

    @GetMapping
    public ResponseEntity<List<DrinkIngredientDto>> getDrinkIngredients() {
        return ResponseEntity.ok(drinkIngredientFacade.getDrinkIngredients());
    }

    @GetMapping(value = "for-drink/{drinkId}")
    public ResponseEntity<List<DrinkIngredientDto>> getDrinkIngredientsForDrink(@PathVariable Long drinkId) {
        return ResponseEntity.ok(drinkIngredientFacade.getDrinkIngredientsForDrink(drinkId));
    }

    @GetMapping("{drinkIngredientId}")
    public DrinkIngredientDto getDrinkIngredient(@PathVariable Long drinkIngredientId) throws DrinkIngredientNotFoundException {
        return drinkIngredientFacade.getDrinkIngredient(drinkIngredientId);
    }

    @DeleteMapping(value = "{drinkIngredientId}")
    public ResponseEntity<Void> deleteDrinkIngredient(@PathVariable Long drinkIngredientId) {
        drinkIngredientFacade.deleteDrinkIngredient(drinkIngredientId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DrinkIngredientDto> saveDrinkIngredient(@RequestBody DrinkIngredientDto drinkIngredientDto)
            throws DrinkNotFoundException, IdFoundException {
        DrinkIngredientDto savedDrinkIngredientDto = drinkIngredientFacade.saveDrinkIngredient(drinkIngredientDto);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(savedDrinkIngredientDto.getId()).toUri();
        return ResponseEntity.created(location).body(savedDrinkIngredientDto);
    }

    @PutMapping
    public ResponseEntity<DrinkIngredientDto> updateDrinkIngredient(@RequestBody DrinkIngredientDto drinkIngredientDto)
            throws DrinkNotFoundException, IdNotFoundException {
        return ResponseEntity.ok(drinkIngredientFacade.updateDrinkIngredient(drinkIngredientDto));
    }
}
