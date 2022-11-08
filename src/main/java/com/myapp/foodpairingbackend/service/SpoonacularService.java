package com.myapp.foodpairingbackend.service;

import com.myapp.foodpairingbackend.client.SpoonacularClient;
import com.myapp.foodpairingbackend.domain.dto.SpoonacularDishDto;
import com.myapp.foodpairingbackend.domain.dto.SpoonacularResultDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SpoonacularService {

    private final SpoonacularClient spoonacularClient;

    public List<SpoonacularDishDto> getSpoonacularDishes(String nameFragment) {
        SpoonacularResultDto result = spoonacularClient.getDishesFromExternalApiDb(nameFragment);
        return result.getResultList();
    }
}
