package com.myapp.foodpairingbackend.dataprovider;

import com.myapp.foodpairingbackend.domain.dto.CompositionDto;
import com.myapp.foodpairingbackend.domain.dto.DrinkIngredientDto;
import com.myapp.foodpairingbackend.domain.entity.Drink;
import com.myapp.foodpairingbackend.exception.ComponentNotFoundException;
import com.myapp.foodpairingbackend.service.DrinkService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DrinkProvider {

    private final DrinkService drinkService;

    public Drink fetchDrink(final CompositionDto compositionDto) throws ComponentNotFoundException {
        return drinkService.getDrink(compositionDto.getDrinkId());
    }

    public Drink fetchDrink(final DrinkIngredientDto drinkIngredientDto) throws ComponentNotFoundException {
        return drinkService.getDrink(drinkIngredientDto.getDrinkId());
    }
}
