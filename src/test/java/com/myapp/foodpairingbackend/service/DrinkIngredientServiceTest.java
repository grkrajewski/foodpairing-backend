package com.myapp.foodpairingbackend.service;

import com.myapp.foodpairingbackend.domain.entity.Drink;
import com.myapp.foodpairingbackend.domain.entity.DrinkIngredient;
import com.myapp.foodpairingbackend.exception.ComponentNotFoundException;
import com.myapp.foodpairingbackend.exception.IdException;
import com.myapp.foodpairingbackend.repository.DrinkIngredientRepository;
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
class DrinkIngredientServiceTest {

    @Autowired
    private DrinkIngredientService drinkIngredientService;

    @MockBean
    private DrinkRepository drinkRepository;

    @MockBean
    private DrinkIngredientRepository drinkIngredientRepository;

    Drink drink;
    DrinkIngredient drinkIngredient;

    @BeforeEach
    void setup() {
        //Given
        drink = Drink.builder()
                .id(1L).externalSystemId("20").name("test name drink").alcoholic("test alcoholic")
                .glass("test glass").instructions("test instructions").drinkIngredientList(List.of())
                .build();

        drinkIngredient = DrinkIngredient.builder()
                .id(2L).name("test name ingredient").measure("test measure").drink(drink)
                .build();
    }

    @Test
    void testGetDrinkIngredients() {
        //Given
        when(drinkIngredientRepository.findAll()).thenReturn(List.of(drinkIngredient));

        //When
        List<DrinkIngredient> drinkIngredients = drinkIngredientService.getDrinkIngredients();

        //Then
        assertEquals(1, drinkIngredients.size());
        verify(drinkIngredientRepository, times(1)).findAll();
    }

    @Test
    void testGetDrinkIngredients_ShouldFetchEmptyList() {
        //Given
        when(drinkIngredientRepository.findAll()).thenReturn(List.of());

        //When
        List<DrinkIngredient> drinkIngredients = drinkIngredientService.getDrinkIngredients();

        //Then
        assertEquals(0, drinkIngredients.size());
        verify(drinkIngredientRepository, times(1)).findAll();
    }

    @Test
    void testGetDrinkIngredientsForDrink() throws ComponentNotFoundException {
        //Given
        when(drinkRepository.existsById(drink.getId())).thenReturn(true);
        when(drinkIngredientRepository.findByDrinkId(drink.getId())).thenReturn(List.of(drinkIngredient));

        //When
        List<DrinkIngredient> savedDrinkIngredientList = drinkIngredientService.getDrinkIngredientsForDrink(drink.getId());

        //Then
        assertEquals(1, savedDrinkIngredientList.size());
        verify(drinkIngredientRepository, times(1)).findByDrinkId(1L);
    }

    @Test
    void testGetDrinkIngredientsForDrink_ShouldGetEmptyList() throws ComponentNotFoundException {
        //Given
        when(drinkRepository.existsById(drink.getId())).thenReturn(true);
        when(drinkIngredientRepository.findByDrinkId(drink.getId())).thenReturn(List.of());

        //When
        List<DrinkIngredient> savedDrinkIngredientList = drinkIngredientService.getDrinkIngredientsForDrink(drink.getId());

        //Then
        assertEquals(0, savedDrinkIngredientList.size());
        verify(drinkIngredientRepository, times(1)).findByDrinkId(1L);
    }

    @Test
    void testGetDrinkIngredientsForDrink_ShouldThrowComponentNotFoundException() {
        //Given
        when(drinkRepository.existsById(1L)).thenReturn(false);

        //When & Then
        assertThrows(ComponentNotFoundException.class, () -> drinkIngredientService.getDrinkIngredientsForDrink(1L));
    }

    @Test
    void testGetDrinkIngredient() throws ComponentNotFoundException {
        //Given
        when(drinkIngredientRepository.findById(drinkIngredient.getId())).thenReturn(Optional.ofNullable(drinkIngredient));

        //When
        DrinkIngredient savedDrinkIngredient = drinkIngredientService.getDrinkIngredient(drinkIngredient.getId());

        //Then
        assertEquals(2L, savedDrinkIngredient.getId());
        assertEquals("test name ingredient", savedDrinkIngredient.getName());
        assertEquals("test measure", savedDrinkIngredient.getMeasure());
        assertNotNull(drinkIngredient.getDrink());
        verify(drinkIngredientRepository, times(1)).findById(2L);
    }

    @Test
    void testDeleteDrinkIngredient() throws ComponentNotFoundException {
        //Given
        doNothing().when(drinkIngredientRepository).deleteById(drinkIngredient.getId());

        //When
        drinkIngredientService.deleteDrinkIngredient(drinkIngredient.getId());

        //Then
        verify(drinkIngredientRepository, times(1)).deleteById(2L);
    }

    @Test
    void testSaveDrinkIngredient() throws IdException {
        //Given
        ReflectionTestUtils.setField(drinkIngredient, "id", null);
        when(drinkIngredientRepository.save(drinkIngredient)).thenAnswer(answer -> {
            ReflectionTestUtils.setField(drinkIngredient, "id", 2L);
            return drinkIngredient;
        });

        //When
        DrinkIngredient savedDrinkIngredient = drinkIngredientService.saveDrinkIngredient(drinkIngredient);

        //Then
        assertEquals(2L, savedDrinkIngredient.getId());
        assertEquals("test name ingredient", savedDrinkIngredient.getName());
        assertEquals("test measure", savedDrinkIngredient.getMeasure());
        assertNotNull(drinkIngredient.getDrink());
        verify(drinkIngredientRepository, times(1)).save(drinkIngredient);
    }

    @Test
    void testSaveDrinkIngredient_ShouldThrowIdException() {
        //When & Then
        assertThrows(IdException.class, () -> drinkIngredientService.saveDrinkIngredient(drinkIngredient));
    }

    @Test
    void testUpdateDrinkIngredient() throws IdException, ComponentNotFoundException {
        //Given
        when(drinkIngredientRepository.findById(drinkIngredient.getId())).thenReturn(Optional.ofNullable(drinkIngredient));
        when(drinkIngredientRepository.save(drinkIngredient)).thenAnswer(answer -> {
            ReflectionTestUtils.setField(drinkIngredient, "name", "test updated name ingredient");
            return drinkIngredient;
        });

        //When
        DrinkIngredient updatedDrinkIngredient = drinkIngredientService.updateDrinkIngredient(drinkIngredient);

        //Then
        assertEquals("test updated name ingredient", updatedDrinkIngredient.getName());
        verify(drinkIngredientRepository, times(1)).save(drinkIngredient);
    }

    @Test
    void testUpdateDrinkIngredient_ShouldThrowIdException() {
        //Given
        ReflectionTestUtils.setField(drinkIngredient, "id", null);

        //When & Then
        assertThrows(IdException.class, () -> drinkIngredientService.updateDrinkIngredient(drinkIngredient));
    }
}