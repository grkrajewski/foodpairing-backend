package com.myapp.foodpairingbackend.service;

import com.myapp.foodpairingbackend.domain.entity.Drink;
import com.myapp.foodpairingbackend.domain.entity.DrinkIngredient;
import com.myapp.foodpairingbackend.repository.DrinkIngredientRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
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

    @Test
    void testGetDrinkIngredientsForDrink() {
        //Given
        List<DrinkIngredient> drinkIngredientList = new ArrayList<>();

        Drink drink = Drink.builder()
                .id(null).externalSystemId("2").name("test name drink").alcoholic("test alcoholic")
                .glass("test glass").instructions("test instructions").drinkIngredientList(drinkIngredientList)
                .build();

        drinkService.saveDrink(drink);

        DrinkIngredient drinkIngredient1 = DrinkIngredient.builder().id(1L).name("test name ingredient 1")
                .measure("test measure 1").drink(drink)
                .build();

        DrinkIngredient drinkIngredient2 = DrinkIngredient.builder().id(2L).name("test name ingredient 2")
                .measure("test measure 2").drink(drink)
                .build();

        DrinkIngredient savedDrinkIngredient1 = drinkIngredientService.saveDrinkIngredient(drinkIngredient1);
        DrinkIngredient savedDrinkIngredient2 = drinkIngredientService.saveDrinkIngredient(drinkIngredient2);
        Long savedDrinkId = drink.getId();
        Long drinkIngredient1Id = savedDrinkIngredient1.getId();
        Long drinkIngredient2Id = savedDrinkIngredient2.getId();

        //When
        List<DrinkIngredient> savedDrinkIngredientList = drinkIngredientService.getDrinkIngredientsForDrink(savedDrinkId);

        //Then
        assertTrue(drinkIngredientRepository.existsById(drinkIngredient1Id));
        assertTrue(drinkIngredientRepository.existsById(drinkIngredient2Id));
        assertEquals(2, savedDrinkIngredientList.size());
    }

    @Test
    void testDeleteDrinkIngredient() {
        //Given
        List<DrinkIngredient> drinkIngredientList = new ArrayList<>();

        Drink drink = Drink.builder()
                .id(null).externalSystemId("2").name("test name drink").alcoholic("test alcoholic")
                .glass("test glass").instructions("test instructions").drinkIngredientList(drinkIngredientList)
                .build();

        DrinkIngredient drinkIngredient1 = DrinkIngredient.builder().id(1L).name("test name ingredient 1")
                .measure("test measure 1").drink(drink)
                .build();

        DrinkIngredient savedDrinkIngredient1 = drinkIngredientService.saveDrinkIngredient(drinkIngredient1);
        Long drinkIngredient1Id = savedDrinkIngredient1.getId();

        //When
        drinkIngredientService.deleteDrinkIngredient(drinkIngredient1Id);

        //Then
        assertFalse(drinkIngredientRepository.existsById(drinkIngredient1Id));
    }
}