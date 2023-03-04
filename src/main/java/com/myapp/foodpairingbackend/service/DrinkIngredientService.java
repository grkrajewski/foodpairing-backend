package com.myapp.foodpairingbackend.service;

import com.myapp.foodpairingbackend.domain.entity.DrinkIngredient;
import com.myapp.foodpairingbackend.exception.DrinkIngredientNotFoundException;
import com.myapp.foodpairingbackend.repository.DrinkIngredientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DrinkIngredientService {

    private final DrinkIngredientRepository drinkIngredientRepository;

    public List<DrinkIngredient> getDrinkIngredients() {
        return drinkIngredientRepository.findAll();
    }

    public List<DrinkIngredient> getDrinkIngredientsForDrink(final Long drinkId) {
        return drinkIngredientRepository.findByDrinkId(drinkId);
    }

    public DrinkIngredient getDrinkIngredient(final Long drinkIngredientId) throws DrinkIngredientNotFoundException {
        return drinkIngredientRepository.findById(drinkIngredientId).orElseThrow(DrinkIngredientNotFoundException::new);
    }

    public void deleteDrinkIngredient(final Long drinkIngredientId) {
        drinkIngredientRepository.deleteById(drinkIngredientId);
    }

    public DrinkIngredient saveDrinkIngredient(final DrinkIngredient drinkIngredient) {
        return drinkIngredientRepository.save(drinkIngredient);
    }
}
