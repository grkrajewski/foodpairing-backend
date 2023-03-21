package com.myapp.foodpairingbackend.service;

import com.myapp.foodpairingbackend.domain.entity.DrinkIngredient;
import com.myapp.foodpairingbackend.exception.ComponentNotFoundException;
import com.myapp.foodpairingbackend.repository.DrinkIngredientRepository;
import com.myapp.foodpairingbackend.repository.DrinkRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DrinkIngredientService {

    private final DrinkIngredientRepository drinkIngredientRepository;
    private final DrinkRepository drinkRepository;

    public List<DrinkIngredient> getDrinkIngredients() {
        return drinkIngredientRepository.findAll();
    }

    public List<DrinkIngredient> getDrinkIngredientsForDrink(final Long drinkId) throws ComponentNotFoundException {
        if (drinkRepository.existsById(drinkId)) {
            return drinkIngredientRepository.findByDrinkId(drinkId);
        }
        throw new ComponentNotFoundException(ComponentNotFoundException.DRINK);

    }

    public DrinkIngredient getDrinkIngredient(final Long drinkIngredientId) throws ComponentNotFoundException {
        return drinkIngredientRepository.findById(drinkIngredientId)
                .orElseThrow(() -> new ComponentNotFoundException(ComponentNotFoundException.DRINK_INGREDIENT));
    }

    public void deleteDrinkIngredient(final Long drinkIngredientId) {
        drinkIngredientRepository.deleteById(drinkIngredientId);
    }

    public DrinkIngredient saveDrinkIngredient(final DrinkIngredient drinkIngredient) {
        return drinkIngredientRepository.save(drinkIngredient);
    }
}
