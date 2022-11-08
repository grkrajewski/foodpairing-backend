package com.myapp.foodpairingbackend.domain.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class DishDto {

    private Long id;
    private Long externalSystemId;
    private String name;
    private int readyInMinutes;
    private int servings;
    private String recipeUrl;
    private List<CompositionDto> compositionList;
}
