package com.myapp.foodpairingbackend.service;

import com.myapp.foodpairingbackend.client.TheCocktailDbClient;
import com.myapp.foodpairingbackend.domain.dto.TheCocktailDbDrinkDto;
import com.myapp.foodpairingbackend.domain.dto.TheCocktailDbResultDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TheCocktailDbService {

    private final TheCocktailDbClient theCocktailDbClient;

    public List<TheCocktailDbDrinkDto> getTheCocktailDrink() {
        TheCocktailDbResultDto result = theCocktailDbClient.getRandomDrinkFromExternalApiDb();
        return result.getResultList();
    }
}
