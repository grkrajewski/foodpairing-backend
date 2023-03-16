package com.myapp.foodpairingbackend.mapper;

import com.myapp.foodpairingbackend.domain.dto.DrinkDto;
import com.myapp.foodpairingbackend.domain.entity.Drink;
import com.myapp.foodpairingbackend.exception.ComponentNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DrinkMapper {

    private final DrinkIngredientMapper drinkIngredientMapper;

    public Drink mapToDrink(final DrinkDto drinkDto) throws ComponentNotFoundException {
        Drink drink = Drink.builder()
                .id(drinkDto.getId())
                .externalSystemId(drinkDto.getExternalSystemId())
                .name(drinkDto.getName())
                .alcoholic(drinkDto.getAlcoholic())
                .glass(drinkDto.getGlass())
                .instructions(drinkDto.getInstructions())
                .drinkIngredientList(drinkIngredientMapper.mapToDrinkIngredientList(drinkDto.getDrinkIngredientList()))
                .build();
        return drink;
    }

    public DrinkDto mapToDrinkDto(final Drink drink) {
        DrinkDto drinkDto = DrinkDto.builder()
                .id(drink.getId())
                .externalSystemId(drink.getExternalSystemId())
                .name(drink.getName())
                .alcoholic(drink.getAlcoholic())
                .glass(drink.getGlass())
                .instructions(drink.getInstructions())
                .drinkIngredientList(drinkIngredientMapper.mapToDrinkIngredientDtoList(drink.getDrinkIngredientList()))
                .build();
        return drinkDto;
    }

    public List<DrinkDto> mapToDrinkDtoList(final List<Drink> drinkList) {
        return drinkList.stream()
                .map(this::mapToDrinkDto)
                .collect(Collectors.toList());
    }
}
