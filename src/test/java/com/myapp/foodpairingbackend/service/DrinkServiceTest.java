package com.myapp.foodpairingbackend.service;

import com.myapp.foodpairingbackend.domain.entity.Drink;
import com.myapp.foodpairingbackend.exception.ComponentNotFoundException;
import com.myapp.foodpairingbackend.exception.IdException;
import com.myapp.foodpairingbackend.repository.DrinkRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class DrinkServiceTest {

    @Autowired
    private DrinkService drinkService;

    @MockBean
    private DrinkRepository drinkRepository;

    Drink drink;

    @BeforeEach
    void setup() {
        //Given
        drink = Drink.builder()
                .id(1L).externalSystemId("20").name("test name drink").alcoholic("test alcoholic")
                .glass("test glass").instructions("test instructions").drinkIngredientList(List.of())
                .build();
    }

    @Test
    void testGetDrinks() {
        //Given
        when(drinkRepository.findAll()).thenReturn(List.of(drink));

        //When
        List<Drink> drinks = drinkService.getDrinks();

        //Then
        assertEquals(1, drinks.size());
        verify(drinkRepository, times(1)).findAll();
    }

    @Test
    void testGetDrinks_ShouldFetchEmptyList() {
        //Given
        when(drinkRepository.findAll()).thenReturn(List.of());

        //When
        List<Drink> drinks = drinkService.getDrinks();

        //Then
        assertEquals(0, drinks.size());
        verify(drinkRepository, times(1)).findAll();
    }

    @Test
    void testGetDrink() throws ComponentNotFoundException {
        //Given
        when(drinkRepository.findById(drink.getId())).thenReturn(Optional.ofNullable(drink));

        //When
        Drink savedDrink = drinkService.getDrink(drink.getId());

        //Then
        assertEquals(1L, savedDrink.getId());
        assertEquals("20", savedDrink.getExternalSystemId());
        assertEquals("test name drink", savedDrink.getName());
        assertEquals("test alcoholic", savedDrink.getAlcoholic());
        assertEquals("test glass", savedDrink.getGlass());
        assertEquals("test instructions", savedDrink.getInstructions());
        assertEquals(0, savedDrink.getDrinkIngredientList().size());
        verify(drinkRepository, times(1)).findById(1L);
    }

    @Test
    void testDeleteDrink() throws ComponentNotFoundException {
        //Given
        doNothing().when(drinkRepository).deleteById(drink.getId());

        //When
        drinkService.deleteDrink(drink.getId());

        //Then
        verify(drinkRepository, times(1)).deleteById(1L);
    }

    @Test
    void testSaveDrink() throws IdException {
        //Given
        ReflectionTestUtils.setField(drink, "id", null);
        when(drinkRepository.save(drink)).thenAnswer(answer -> {
            ReflectionTestUtils.setField(drink, "id", 1L);
            return drink;
        });

        //When
        Drink savedDrink = drinkService.saveDrink(drink);

        //Then
        assertEquals(1L, savedDrink.getId());
        assertEquals("20", savedDrink.getExternalSystemId());
        assertEquals("test name drink", savedDrink.getName());
        assertEquals("test alcoholic", savedDrink.getAlcoholic());
        assertEquals("test glass", savedDrink.getGlass());
        assertEquals("test instructions", savedDrink.getInstructions());
        assertEquals(0, savedDrink.getDrinkIngredientList().size());
        verify(drinkRepository, times(1)).save(drink);
    }

    @Test
    void testSaveDrink_ShouldThrowIdException() {
        //When & Then
        assertThrows(IdException.class, () -> drinkService.saveDrink(drink));
    }

    @Test
    void testUpdateDrink() throws IdException, ComponentNotFoundException {
        //Given
        when(drinkRepository.findById(drink.getId())).thenReturn(Optional.ofNullable(drink));
        when(drinkRepository.save(drink)).thenAnswer(answer -> {
            ReflectionTestUtils.setField(drink, "name", "test updated name drink");
            return drink;
        });

        //When
        Drink updatedDrink = drinkService.updateDrink(drink);

        //Then
        assertEquals("test updated name drink", updatedDrink.getName());
        verify(drinkRepository, times(1)).save(drink);
    }

    @Test
    void testUpdateDrink_ShouldThrowIdException() {
        //Given
        ReflectionTestUtils.setField(drink, "id", null);

        //When & Then
        assertThrows(IdException.class, () -> drinkService.updateDrink(drink));
    }
}