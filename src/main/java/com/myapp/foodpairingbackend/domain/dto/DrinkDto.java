package com.myapp.foodpairingbackend.domain.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class DrinkDto {

    private Long id;
    private String externalSystemId;
    private String name;
    private String alcoholic;
    private String glass;
    private String instructions;
    private List<DrinkIngredientDto> drinkIngredientList;
}
