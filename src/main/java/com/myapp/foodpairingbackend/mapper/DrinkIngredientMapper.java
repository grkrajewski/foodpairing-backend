package com.myapp.foodpairingbackend.mapper;

import com.myapp.foodpairingbackend.domain.dto.DrinkIngredientDto;
import com.myapp.foodpairingbackend.domain.entity.DrinkIngredient;
import com.myapp.foodpairingbackend.exception.DrinkNotFoundException;
import com.myapp.foodpairingbackend.service.DrinkService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DrinkIngredientMapper {

    private final DrinkService drinkService;

    public DrinkIngredient mapToDrinkIngredient(final DrinkIngredientDto drinkIngredientDto) throws DrinkNotFoundException {
        DrinkIngredient drinkIngredient = DrinkIngredient.builder()
                .id(drinkIngredientDto.getId())
                .name(drinkIngredientDto.getName())
                .measure(drinkIngredientDto.getMeasure())
                .drink(drinkService.getDrink(drinkIngredientDto.getDrinkId()))
                .build();
        return drinkIngredient;
    }

    public DrinkIngredientDto mapToDrinkIngredientDto(final DrinkIngredient drinkIngredient) {
        DrinkIngredientDto drinkIngredientDto = DrinkIngredientDto.builder()
                .id(drinkIngredient.getId())
                .name(drinkIngredient.getName())
                .measure(drinkIngredient.getMeasure())
                .drinkId(drinkIngredient.getDrink().getId())
                .build();
        return drinkIngredientDto;
    }

    public List<DrinkIngredient> mapToDrinkIngredientList(final List<DrinkIngredientDto> drinkIngredientDtoList) throws DrinkNotFoundException {
        List<DrinkIngredient> drinkIngredientList = new ArrayList<>();
        for (DrinkIngredientDto drinkIngredientDto : drinkIngredientDtoList) {
            DrinkIngredient drinkIngredient = mapToDrinkIngredient(drinkIngredientDto);
            drinkIngredientList.add(drinkIngredient);
        }
        return drinkIngredientList;
    }

    public List<DrinkIngredientDto> mapToDrinkIngredientDtoList(final List<DrinkIngredient> drinkIngredientList) {
        return drinkIngredientList.stream()
                .map(this::mapToDrinkIngredientDto)
                .collect(Collectors.toList());
    }
}
