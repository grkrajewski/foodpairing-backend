package com.myapp.foodpairingbackend.service;

import com.myapp.foodpairingbackend.domain.entity.Drink;
import com.myapp.foodpairingbackend.domain.entity.DrinkIngredient;
import com.myapp.foodpairingbackend.exception.ComponentNotFoundException;
import com.myapp.foodpairingbackend.exception.IdException;
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
    void testGetDrinkIngredientsForDrink() throws ComponentNotFoundException, IdException {
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
    void testGetDrinkIngredientsForDrinkShouldGetEmptyList() throws ComponentNotFoundException, IdException {
        //Given
        drinkService.saveDrink(drink);
        Long drinkId = drink.getId();

        //When
        List<DrinkIngredient> drinkIngredientList = drinkIngredientService.getDrinkIngredientsForDrink(drinkId);

        //Then
        assertEquals(0, drinkIngredientList.size());
    }

    @Test
    void testGetDrinkIngredient() throws ComponentNotFoundException, IdException {
        //Given
        drinkService.saveDrink(drink);
        drinkIngredientService.saveDrinkIngredient(drinkIngredient);
        Long drinkIngredientId = drinkIngredient.getId();

        //When
        DrinkIngredient savedDrinkIngredient = drinkIngredientService.getDrinkIngredient(drinkIngredientId);

        //Then
        assertTrue(drinkIngredientRepository.existsById(drinkIngredientId));
        assertEquals("test name ingredient", savedDrinkIngredient.getName());
        assertEquals("test measure", savedDrinkIngredient.getMeasure());
        assertEquals("test name drink", savedDrinkIngredient.getDrink().getName());
    }

    @Test
    void testDeleteDrinkIngredient() throws ComponentNotFoundException, IdException {
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
    void testDeleteDrinkIngredientShouldThrowComponentNotFoundException() {
        //When & Then
        assertThrows(ComponentNotFoundException.class, () -> drinkIngredientService.deleteDrinkIngredient(1L));
    }

    @Test
    void testSaveDrinkIngredient() throws IdException {
        //Given
        drinkService.saveDrink(drink);

        //When
        drinkIngredientService.saveDrinkIngredient(drinkIngredient);
        Long drinkIngredientId = drinkIngredient.getId();

        //Then
        assertTrue(drinkIngredientRepository.existsById(drinkIngredientId));
    }

    @Test
    void testSaveDrinkIngredientShouldThrowIdException() {
        //Given
        DrinkIngredient drinkIngredientWithId = DrinkIngredient.builder()
                .id(1L).name("test name ingredient").measure("test measure").drink(drink)
                .build();

        //When & Then
        assertThrows(IdException.class, () -> drinkIngredientService.saveDrinkIngredient(drinkIngredientWithId));
    }

    @Test
    void testUpdateDrinkIngredient() throws IdException, ComponentNotFoundException {
        //Given
        drinkService.saveDrink(drink);
        drinkIngredientService.saveDrinkIngredient(drinkIngredient);
        Long drinkIngredientId = drinkIngredient.getId();
        DrinkIngredient nameUpdatedDrinkIngredient = DrinkIngredient.builder()
                .id(drinkIngredientId).name("test updated name ingredient").measure("test measure").drink(drink)
                .build();

        //When
        DrinkIngredient updatedDrinkIngredient = drinkIngredientService.updateDrinkIngredient(nameUpdatedDrinkIngredient);

        //Then
        assertEquals("test updated name ingredient", updatedDrinkIngredient.getName());
    }

    @Test
    void testUpdateDrinkIngredientShouldThrowIdException() {

        //When & Then
        assertThrows(IdException.class, () -> drinkIngredientService.updateDrinkIngredient(drinkIngredient));
    }
}