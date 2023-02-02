package com.myapp.foodpairingbackend.service;

import com.myapp.foodpairingbackend.domain.entity.Drink;
import com.myapp.foodpairingbackend.domain.entity.DrinkIngredient;
import com.myapp.foodpairingbackend.repository.DrinkIngredientRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
@SpringBootTest
class DrinkIngredientServiceTest {

    @Autowired
    private DrinkService drinkService;

    @Autowired
    private DrinkIngredientService drinkIngredientService;

    @Autowired
    private DrinkIngredientRepository drinkIngredientRepository;

    //Given - data preparation
    Drink drink = Drink.builder()
            .id(null).externalSystemId("2").name("test name drink").alcoholic("test alcoholic")
            .glass("test glass").instructions("test instructions").drinkIngredientList(List.of())
            .build();

    DrinkIngredient drinkIngredient = DrinkIngredient.builder()
            .id(null).name("test name ingredient").measure("test measure").drink(drink)
            .build();

    @Test
    void testGetDrinkIngredientsForDrink() {
        //Given
        drinkService.saveDrink(drink);
        drinkIngredientService.saveDrinkIngredient(drinkIngredient);
        Long drinkId = drink.getId();
        Long drinkIngredientId = drinkIngredient.getId();

        //When
        List<DrinkIngredient> savedDrinkIngredientList = drinkIngredientService.getDrinkIngredientsForDrink(drinkId);

        //Then
        assertTrue(drinkIngredientRepository.existsById(drinkIngredientId));
        assertEquals(1, savedDrinkIngredientList.size());
        assertEquals("test name ingredient", savedDrinkIngredientList.get(0).getName());
        assertEquals("test measure", savedDrinkIngredientList.get(0).getMeasure());
        assertEquals("test name drink", savedDrinkIngredientList.get(0).getDrink().getName());
    }

    @Test
    void testDeleteDrinkIngredient() {
        //Given
        drinkService.saveDrink(drink);
        drinkIngredientService.saveDrinkIngredient(drinkIngredient);
        Long drinkIngredientId = drinkIngredient.getId();

        //When
        drinkIngredientService.deleteDrinkIngredient(drinkIngredientId);

        //Then
        assertFalse(drinkIngredientRepository.existsById(drinkIngredientId));
    }

    @Test
    void testSaveDrinkIngredient() {
        //Given
        drinkService.saveDrink(drink);

        //When
        drinkIngredientService.saveDrinkIngredient(drinkIngredient);
        Long drinkIngredientId = drinkIngredient.getId();

        //Then
        assertTrue(drinkIngredientRepository.existsById(drinkIngredientId));
    }
}