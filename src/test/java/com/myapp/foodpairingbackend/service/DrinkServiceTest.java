package com.myapp.foodpairingbackend.service;

import com.myapp.foodpairingbackend.domain.entity.Drink;
import com.myapp.foodpairingbackend.exception.DrinkNotFoundException;
import com.myapp.foodpairingbackend.repository.DrinkRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
@SpringBootTest
class DrinkServiceTest {

    @Autowired
    private DrinkService drinkService;

    @Autowired
    private DrinkRepository drinkRepository;

    @Test
    void testGetDrink() throws DrinkNotFoundException {
        //Given
        Drink drink = Drink.builder()
                .id(null).externalSystemId("2").name("test name drink").alcoholic("test alcoholic")
                .glass("test glass").instructions("test instructions").drinkIngredientList(new ArrayList<>())
                .build();

        drinkService.saveDrink(drink);
        Long drinkId = drink.getId();

        //When
        Drink savedDrink = drinkService.getDrink(drinkId);

        //Then
        assertTrue(drinkRepository.existsById(drinkId));
        assertEquals("test name drink", savedDrink.getName());
    }

    @Test
    void testDeleteDrink() {
        //Given
        Drink drink = Drink.builder()
                .id(null).externalSystemId("2").name("test name drink").alcoholic("test alcoholic")
                .glass("test glass").instructions("test instructions").drinkIngredientList(new ArrayList<>())
                .build();

        drinkService.saveDrink(drink);
        Long drinkId = drink.getId();

        //When
        drinkService.deleteDrink(drinkId);

        //Then
        assertFalse(drinkRepository.existsById(drinkId));
    }
}