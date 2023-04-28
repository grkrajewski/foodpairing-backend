package com.myapp.foodpairingbackend.service;

import com.myapp.foodpairingbackend.domain.entity.Drink;
import com.myapp.foodpairingbackend.exception.ComponentNotFoundException;
import com.myapp.foodpairingbackend.exception.IdException;
import com.myapp.foodpairingbackend.repository.DrinkRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@Transactional
@SpringBootTest
class DrinkServiceTest {

    @Autowired
    private DrinkService drinkService;

    @Autowired
    private DrinkRepository drinkRepository;

    @InjectMocks
    private DrinkService drinkServiceMock;

    @Mock
    private DrinkRepository drinkRepositoryMock;

    //Given - data preparation
    Drink drink = Drink.builder()
            .id(null).externalSystemId("2").name("test name drink").alcoholic("test alcoholic")
            .glass("test glass").instructions("test instructions").drinkIngredientList(List.of())
            .build();

    @Test
    void testGetDrinks() {
        //Given
        when(drinkRepositoryMock.findAll()).thenReturn(List.of(drink));

        //When
        List<Drink> drinks = drinkServiceMock.getDrinks();

        //Then
        assertEquals(1, drinks.size());
        verify(drinkRepositoryMock, times(1)).findAll();
    }

    @Test
    void testGetDrinks_ShouldFetchEmptyList() {
        //When
        List<Drink> drinks = drinkServiceMock.getDrinks();

        //Then
        assertEquals(0, drinks.size());
        verify(drinkRepositoryMock, times(1)).findAll();
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
        assertEquals("2", savedDrink.getExternalSystemId());
        assertEquals("test name drink", savedDrink.getName());
        assertEquals("test alcoholic", savedDrink.getAlcoholic());
        assertEquals("test glass", savedDrink.getGlass());
        assertEquals("test instructions", savedDrink.getInstructions());
        assertEquals(0, savedDrink.getDrinkIngredientList().size());
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
        Long drinkId = drink.getId();

        //Then
        assertTrue(drinkRepository.existsById(drinkId));
    }

    @Test
    void testSaveDrink_ShouldThrowIdException() {
        //Given
        Drink drinkWithId = Drink.builder()
                .id(1L).externalSystemId("2").name("test name drink").alcoholic("test alcoholic")
                .glass("test glass").instructions("test instructions").drinkIngredientList(List.of())
                .build();

        //When & Then
        assertThrows(IdException.class, () -> drinkService.saveDrink(drinkWithId));
    }

    @Test
    void testUpdateDrink() throws IdException, ComponentNotFoundException {
        //Given
        drinkService.saveDrink(drink);
        Long drinkId = drink.getId();
        Drink nameUpdatedDrink = Drink.builder()
                .id(drinkId).externalSystemId("2").name("test updated name drink").alcoholic("test alcoholic")
                .glass("test glass").instructions("test instructions").drinkIngredientList(List.of())
                .build();

        //When
        Drink updatedDrink = drinkService.updateDrink(nameUpdatedDrink);

        //Then
        assertEquals("test updated name drink", updatedDrink.getName());
    }

    @Test
    void testUpdateDrink_ShouldThrowIdException() {
        //When & Then
        assertThrows(IdException.class, () -> drinkService.updateDrink(drink));
    }
}