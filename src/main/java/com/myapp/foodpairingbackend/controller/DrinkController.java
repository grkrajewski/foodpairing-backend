package com.myapp.foodpairingbackend.controller;

import com.myapp.foodpairingbackend.domain.dto.DrinkDto;
import com.myapp.foodpairingbackend.exception.DrinkNotFoundException;
import com.myapp.foodpairingbackend.exception.IdFoundException;
import com.myapp.foodpairingbackend.exception.IdNotFoundException;
import com.myapp.foodpairingbackend.facade.DrinkFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/foodpairing/v1/drinks")
@RequiredArgsConstructor
public class DrinkController {

    private final DrinkFacade drinkFacade;

    @GetMapping
    public ResponseEntity<List<DrinkDto>> getDrinks() {
        return ResponseEntity.ok(drinkFacade.getDrinks());
    }

    @GetMapping(value = "{drinkId}")
    public ResponseEntity<DrinkDto> getDrink(@PathVariable Long drinkId) throws DrinkNotFoundException {
        return ResponseEntity.ok(drinkFacade.getDrink(drinkId));
    }

    @DeleteMapping(value = "{drinkId}")
    public ResponseEntity<Void> deleteDrink(@PathVariable Long drinkId) {
        drinkFacade.deleteDrink(drinkId);
        return ResponseEntity.ok().build();
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DrinkDto> saveDrinkInDb(@RequestBody DrinkDto drinkDto) throws DrinkNotFoundException, IdFoundException {
        return ResponseEntity.ok(drinkFacade.saveDrinkInDb(drinkDto));
    }

    @PutMapping
    public ResponseEntity<DrinkDto> updateDrink(@RequestBody DrinkDto drinkDto) throws DrinkNotFoundException, IdNotFoundException {
        return ResponseEntity.ok(drinkFacade.updateDrink(drinkDto));
    }
}
