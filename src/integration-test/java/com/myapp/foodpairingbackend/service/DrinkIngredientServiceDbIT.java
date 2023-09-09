package com.myapp.foodpairingbackend.service;

import com.myapp.foodpairingbackend.domain.entity.Drink;
import com.myapp.foodpairingbackend.domain.entity.DrinkIngredient;
import com.myapp.foodpairingbackend.exception.ComponentNotFoundException;
import com.myapp.foodpairingbackend.exception.IdException;
import com.myapp.foodpairingbackend.repository.DrinkIngredientRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
@TestPropertySource("/application-test.properties")
public class DrinkIngredientServiceDbIT {

    @Autowired
    private DrinkService drinkService;

    @Autowired
    private DrinkIngredientService drinkIngredientService;

    @Autowired
    private DrinkIngredientRepository drinkIngredientRepository;

    private Drink drink;
    private DrinkIngredient drinkIngredient;

    @BeforeEach
    void setup() {
        //Given
        drink = Drink.builder()
                .id(null).externalSystemId("20").name("test name drink").alcoholic("test alcoholic")
                .glass("test glass").instructions("test instructions").drinkIngredientList(List.of())
                .build();

        drinkIngredient = DrinkIngredient.builder()
                .id(null).name("test name ingredient").measure("test measure").drink(drink)
                .build();
    }

    @Test
    void testGetDrinkIngredients() throws IdException {
        //Given
        drinkService.saveDrink(drink);
        drinkIngredientService.saveDrinkIngredient(drinkIngredient);

        //When
        List<DrinkIngredient> drinkIngredients = drinkIngredientService.getDrinkIngredients();

        //Then
        assertEquals(1, drinkIngredients.size());
    }

    @Test
    void testGetDrinkIngredients_ShouldFetchEmptyList() {
        //When
        List<DrinkIngredient> drinkIngredients = drinkIngredientService.getDrinkIngredients();

        //Then
        assertTrue(drinkIngredients.isEmpty());
    }

    @Test
    void testGetDrinkIngredientsForDrink() throws ComponentNotFoundException, IdException {
        //Given
        drinkService.saveDrink(drink);
        drinkIngredientService.saveDrinkIngredient(drinkIngredient);

        //When
        List<DrinkIngredient> savedDrinkIngredientList = drinkIngredientService.getDrinkIngredientsForDrink(drink.getId());

        //Then
        assertTrue(drinkIngredientRepository.existsById(drinkIngredient.getId()));
        assertEquals(1, savedDrinkIngredientList.size());
        assertEquals("test name ingredient", savedDrinkIngredientList.get(0).getName());
        assertEquals("test measure", savedDrinkIngredientList.get(0).getMeasure());
        assertEquals("test name drink", savedDrinkIngredientList.get(0).getDrink().getName());
    }

    @Test
    void testGetDrinkIngredientsForDrink_ShouldGetEmptyList() throws ComponentNotFoundException, IdException {
        //Given
        drinkService.saveDrink(drink);

        //When
        List<DrinkIngredient> drinkIngredientList = drinkIngredientService.getDrinkIngredientsForDrink(drink.getId());

        //Then
        assertTrue(drinkIngredientList.isEmpty());
    }

    @Test
    void testGetDrinkIngredientsForDrink_ShouldThrowComponentNotFoundException() {
        //When & Then
        assertThrows(ComponentNotFoundException.class, () -> drinkIngredientService.getDrinkIngredientsForDrink(1L));
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
    void testDeleteDrinkIngredient_ShouldThrowComponentNotFoundException() {
        //When & Then
        assertThrows(ComponentNotFoundException.class, () -> drinkIngredientService.deleteDrinkIngredient(1L));
    }

    @Test
    void testSaveDrinkIngredient() throws IdException {
        //Given
        drinkService.saveDrink(drink);

        //When
        drinkIngredientService.saveDrinkIngredient(drinkIngredient);

        //Then
        assertTrue(drinkIngredientRepository.existsById(drinkIngredient.getId()));
    }

    @Test
    void testSaveDrinkIngredient_ShouldThrowIdException() {
        //Given
        ReflectionTestUtils.setField(drinkIngredient, "id", 2L);

        //When & Then
        assertThrows(IdException.class, () -> drinkIngredientService.saveDrinkIngredient(drinkIngredient));
    }

    @Test
    void testUpdateDrinkIngredient() throws IdException, ComponentNotFoundException {
        //Given
        drinkService.saveDrink(drink);
        drinkIngredientService.saveDrinkIngredient(drinkIngredient);
        DrinkIngredient nameUpdatedDrinkIngredient = DrinkIngredient.builder()
                .id(drinkIngredient.getId()).name("test updated name ingredient").measure("test measure").drink(drink)
                .build();

        //When
        drinkIngredientService.updateDrinkIngredient(nameUpdatedDrinkIngredient);

        //Then
        assertEquals("test updated name ingredient", drinkIngredient.getName());
    }

    @Test
    void testUpdateDrinkIngredient_ShouldThrowIdException() {
        //When & Then
        assertThrows(IdException.class, () -> drinkIngredientService.updateDrinkIngredient(drinkIngredient));
    }
}
