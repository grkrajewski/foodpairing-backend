package com.myapp.foodpairingbackend.service;

import com.myapp.foodpairingbackend.domain.entity.Drink;
import com.myapp.foodpairingbackend.exception.ComponentNotFoundException;
import com.myapp.foodpairingbackend.exception.IdException;
import com.myapp.foodpairingbackend.repository.DrinkRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Tag("integration-test")
@SpringBootTest
@Transactional
@TestPropertySource("/application-test.properties")
public class DrinkServiceDbIntegrationTest {


    @Autowired
    private DrinkService drinkService;

    @Autowired
    private DrinkRepository drinkRepository;

    private Drink drink;

    @BeforeEach
    void setup() {
        //Given
        drink = Drink.builder()
                .id(null).externalSystemId("20").name("test name drink").alcoholic("test alcoholic")
                .glass("test glass").instructions("test instructions").drinkIngredientList(List.of())
                .build();
    }

    @Test
    void testGetDrinks() throws IdException {
        //Given
        drinkService.saveDrink(drink);

        //When
        List<Drink> drinks = drinkService.getDrinks();

        //Then
        assertEquals(1, drinks.size());
    }

    @Test
    void testGetDrinks_ShouldFetchEmptyList() {
        //When
        List<Drink> drinks = drinkService.getDrinks();

        //Then
        assertTrue(drinks.isEmpty());
    }

    @Test
    void testGetDrink() throws ComponentNotFoundException, IdException {
        //Given
        drinkService.saveDrink(drink);
        Long drinkId = drink.getId();

        //When
        Drink savedDrink = drinkService.getDrink(drinkId);

        //Then
        assertTrue(drinkRepository.existsById(drinkId));
        assertEquals("20", savedDrink.getExternalSystemId());
        assertEquals("test name drink", savedDrink.getName());
        assertEquals("test alcoholic", savedDrink.getAlcoholic());
        assertEquals("test glass", savedDrink.getGlass());
        assertEquals("test instructions", savedDrink.getInstructions());
        assertTrue(savedDrink.getDrinkIngredientList().isEmpty());
    }

    @Test
    void testDeleteDrink() throws ComponentNotFoundException, IdException {
        //Given
        drinkService.saveDrink(drink);
        Long drinkId = drink.getId();

        //When
        drinkService.deleteDrink(drinkId);

        //Then
        assertFalse(drinkRepository.existsById(drinkId));
    }

    @Test
    void testDeleteDrink_ShouldThrowComponentNotFoundException() {
        //When & Then
        assertThrows(ComponentNotFoundException.class, () -> drinkService.deleteDrink(1L));
    }

    @Test
    void testSaveDrink() throws IdException {
        //When
        drinkService.saveDrink(drink);

        //Then
        assertTrue(drinkRepository.existsById(drink.getId()));
    }

    @Test
    void testSaveDrink_ShouldThrowIdException() throws IdException {
        //Given
        ReflectionTestUtils.setField(drink, "id", 1L);

        //When & Then
        assertThrows(IdException.class, () -> drinkService.saveDrink(drink));
    }

    @Test
    void testUpdateDrink() throws IdException, ComponentNotFoundException {
        //Given
        drinkService.saveDrink(drink);
        Drink nameUpdatedDrink = Drink.builder()
                .id(drink.getId()).externalSystemId("20").name("test updated name drink").alcoholic("test alcoholic")
                .glass("test glass").instructions("test instructions").drinkIngredientList(List.of())
                .build();

        //When
        drinkService.updateDrink(nameUpdatedDrink);

        //Then
        assertEquals("test updated name drink", drink.getName());
    }

    @Test
    void testUpdateDrink_ShouldThrowIdException() {
        //When & Then
        assertThrows(IdException.class, () -> drinkService.updateDrink(drink));
    }
}
