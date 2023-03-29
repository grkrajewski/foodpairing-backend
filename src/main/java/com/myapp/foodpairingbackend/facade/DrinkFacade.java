package com.myapp.foodpairingbackend.facade;

import com.myapp.foodpairingbackend.domain.dto.DrinkDto;
import com.myapp.foodpairingbackend.domain.entity.Drink;
import com.myapp.foodpairingbackend.exception.ComponentNotFoundException;
import com.myapp.foodpairingbackend.exception.IdException;
import com.myapp.foodpairingbackend.mapper.DrinkMapper;
import com.myapp.foodpairingbackend.service.DrinkService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class DrinkFacade {

    private final DrinkService drinkService;
    private final DrinkMapper drinkMapper;

    public List<DrinkDto> getDrinks() {
        List<Drink> drinkList = drinkService.getDrinks();
        return drinkMapper.mapToDrinkDtoList(drinkList);
    }

    public DrinkDto getDrink(Long drinkId) throws ComponentNotFoundException {
        Drink drink = drinkService.getDrink(drinkId);
        return drinkMapper.mapToDrinkDto(drink);
    }

    public void deleteDrink(Long drinkId) throws ComponentNotFoundException {
        drinkService.deleteDrink(drinkId);
    }

    public DrinkDto saveDrinkInDb(DrinkDto drinkDto) throws ComponentNotFoundException, IdException {
        Drink drink = drinkMapper.mapToDrink(drinkDto);
        Drink savedDrink = drinkService.saveDrink(drink);
        return drinkMapper.mapToDrinkDto(savedDrink);
    }

    public DrinkDto updateDrink(DrinkDto drinkDto) throws ComponentNotFoundException, IdException {
        Drink drink = drinkMapper.mapToDrink(drinkDto);
        Drink savedDrink = drinkService.updateDrink(drink);
        return drinkMapper.mapToDrinkDto(savedDrink);
    }
}
