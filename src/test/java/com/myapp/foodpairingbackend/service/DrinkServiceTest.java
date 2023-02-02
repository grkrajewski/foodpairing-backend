package com.myapp.foodpairingbackend.service;

import com.myapp.foodpairingbackend.domain.entity.Drink;
import com.myapp.foodpairingbackend.exception.DrinkNotFoundException;
import com.myapp.foodpairingbackend.repository.DrinkRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
@SpringBootTest
class DrinkServiceTest {

    @Autowired
    private DrinkService drinkService;

    @Autowired
    private DrinkRepository drinkRepository;

    //Given - data preparation
    Drink drink = Drink.builder()
            .id(null).externalSystemId("2").name("test name drink").alcoholic("test alcoholic")
            .glass("test glass").instructions("test instructions").drinkIngredientList(List.of())
            .build();

    @Test
    void testGetDrink() throws DrinkNotFoundException {
        //Given
        drinkService.saveDrink(drink);
        Long drinkId = drink.getId();

        //When
        Drink savedDrink = drinkService.getDrink(drinkId);

        //Then
        assertTrue(drinkRepository.existsById(drinkId));
        assertEquals("2", savedDrink.getExternalSystemId());
        assertEquals("test name drink", savedDrink.getName());
        assertEquals("test alcoholic", savedDrink.getAlcoholic());
        assertEquals("test glass", savedDrink.getGlass());
        assertEquals("test instructions", savedDrink.getInstructions());
        assertEquals(0, savedDrink.getDrinkIngredientList().size());
    }

    @Test
    void testDeleteDrink() {
        //Given
        drinkService.saveDrink(drink);
        Long drinkId = drink.getId();

        //When
        drinkService.deleteDrink(drinkId);

        //Then
        assertFalse(drinkRepository.existsById(drinkId));
    }

    @Test
    void testSaveDrink() {
        //When
        drinkService.saveDrink(drink);
        Long drinkId = drink.getId();

        //Then
        assertTrue(drinkRepository.existsById(drinkId));
    }
}