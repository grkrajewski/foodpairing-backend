package com.myapp.foodpairingbackend.service;

import com.myapp.foodpairingbackend.domain.entity.DrinkIngredient;
import com.myapp.foodpairingbackend.repository.DrinkIngredientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DrinkIngredientService {

    private final DrinkIngredientRepository drinkIngredientRepository;

    public List<DrinkIngredient> getDrinkIngredients() {
        return drinkIngredientRepository.findAll();
    }

    public List<DrinkIngredient> getDrinkIngredientsForDrink(final Long drinkId) {
        List<DrinkIngredient> allDrinkIngredientList = drinkIngredientRepository.findAll();
        List<DrinkIngredient> filteredDrinkIngredientList = new ArrayList<>();
        for (DrinkIngredient ingredient : allDrinkIngredientList) {
            if (ingredient.getDrink().getId().equals(drinkId)) {
                filteredDrinkIngredientList.add(ingredient);
            }
        }
        return filteredDrinkIngredientList;
    }

    public void deleteDrinkIngredient(final Long drinkIngredientId) {
        drinkIngredientRepository.deleteById(drinkIngredientId);
    }

    public DrinkIngredient saveDrinkIngredient(final DrinkIngredient drinkIngredient) {
        return drinkIngredientRepository.save(drinkIngredient);
    }
}
