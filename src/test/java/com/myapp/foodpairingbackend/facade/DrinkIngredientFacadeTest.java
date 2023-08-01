package com.myapp.foodpairingbackend.facade;

import com.myapp.foodpairingbackend.domain.dto.DrinkIngredientDto;
import com.myapp.foodpairingbackend.domain.entity.Drink;
import com.myapp.foodpairingbackend.domain.entity.DrinkIngredient;
import com.myapp.foodpairingbackend.exception.ComponentNotFoundException;
import com.myapp.foodpairingbackend.exception.IdException;
import com.myapp.foodpairingbackend.mapper.DrinkIngredientMapper;
import com.myapp.foodpairingbackend.service.DrinkIngredientService;
import com.myapp.foodpairingbackend.service.DrinkService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@SpringBootTest
public class DrinkIngredientFacadeTest {

    @Autowired
    private DrinkIngredientFacade drinkIngredientFacade;

    @MockBean
    private DrinkIngredientService drinkIngredientService;

    @MockBean
    private DrinkService drinkService;

    @SpyBean
    private DrinkIngredientMapper drinkIngredientMapper;

    private Drink drink;
    private DrinkIngredient drinkIngredient;
    private DrinkIngredientDto drinkIngredientDto;

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

        drinkIngredientDto = DrinkIngredientDto.builder()
                .id(2L).name("test name ingredient").measure("test measure").drinkId(drink.getId())
                .build();
    }

    @Test
    void testGetDrinkIngredients() {
        //Given
        when(drinkIngredientService.getDrinkIngredients()).thenReturn(List.of(drinkIngredient));

        //When
        List<DrinkIngredientDto> drinkIngredients = drinkIngredientFacade.getDrinkIngredients();

        //Then
        assertEquals(1, drinkIngredients.size());
        verify(drinkIngredientService, times(1)).getDrinkIngredients();
        verify(drinkIngredientMapper, times(1)).mapToDrinkIngredientDtoList(List.of(drinkIngredient));
    }

    @Test
    void testGetDrinkIngredientsForDrink() throws ComponentNotFoundException {
        //Given
        when(drinkIngredientService.getDrinkIngredientsForDrink(drink.getId())).thenReturn(List.of(drinkIngredient));

        //When
        List<DrinkIngredientDto> drinkIngredients = drinkIngredientFacade.getDrinkIngredientsForDrink(drink.getId());

        //Then
        assertEquals(1, drinkIngredients.size());
        verify(drinkIngredientService, times(1)).getDrinkIngredientsForDrink(1L);
        verify(drinkIngredientMapper, times(1)).mapToDrinkIngredientDtoList(List.of(drinkIngredient));
    }

    @Test
    void testGetDrinkIngredient() throws ComponentNotFoundException {
        //Given
        when(drinkIngredientService.getDrinkIngredient(drinkIngredient.getId())).thenReturn(drinkIngredient);

        //When
        DrinkIngredientDto fetchedDrinkIngredient = drinkIngredientFacade.getDrinkIngredient(drinkIngredient.getId());

        //Then
        assertEquals(2L, fetchedDrinkIngredient.getId());
        assertEquals("test name ingredient", fetchedDrinkIngredient.getName());
        assertEquals("test measure", fetchedDrinkIngredient.getMeasure());
        assertNotNull(drinkIngredient.getDrink());
        verify(drinkIngredientService, times(1)).getDrinkIngredient(2L);
        verify(drinkIngredientMapper, times(1)).mapToDrinkIngredientDto(drinkIngredient);
    }

    @Test
    void testDeleteDrinkIngredient() throws ComponentNotFoundException {
        //Given
        doNothing().when(drinkIngredientService).deleteDrinkIngredient(drinkIngredient.getId());

        //When
        drinkIngredientFacade.deleteDrinkIngredient(drinkIngredient.getId());

        //Then
        verify(drinkIngredientService, times(1)).deleteDrinkIngredient(2L);
    }

    @Test
    void testSaveDrinkIngredient() throws IdException, ComponentNotFoundException {
        //Given
        when(drinkIngredientService.saveDrinkIngredient(any(DrinkIngredient.class))).thenReturn(drinkIngredient);

        //When
        DrinkIngredientDto savedDrinkIngredientDto = drinkIngredientFacade.saveDrinkIngredient(drinkIngredientDto);

        //Then
        assertEquals(2L, savedDrinkIngredientDto.getId());
        assertEquals("test name ingredient", savedDrinkIngredientDto.getName());
        assertEquals("test measure", savedDrinkIngredientDto.getMeasure());
        verify(drinkIngredientService, times(1)).saveDrinkIngredient(any(DrinkIngredient.class));
        verify(drinkIngredientMapper, times(1)).mapToDrinkIngredientDto(drinkIngredient);
        verify(drinkIngredientMapper, times(1)).mapToDrinkIngredient(drinkIngredientDto);
    }

    @Test
    void testUpdateDrinkIngredient() throws ComponentNotFoundException, IdException {
        //Given
        when(drinkIngredientService.updateDrinkIngredient(any(DrinkIngredient.class))).thenAnswer(answer -> {
            ReflectionTestUtils.setField(drinkIngredient, "name", "test updated name ingredient");
            return drinkIngredient;
        });

        //When
        DrinkIngredientDto updatedDrinkIngredientDto = drinkIngredientFacade.updateDrinkIngredient(drinkIngredientDto);

        //Then
        assertEquals(2L, updatedDrinkIngredientDto.getId());
        assertEquals("test updated name ingredient", updatedDrinkIngredientDto.getName());
        assertEquals("test measure", updatedDrinkIngredientDto.getMeasure());
        verify(drinkIngredientService, times(1)).updateDrinkIngredient(any(DrinkIngredient.class));
        verify(drinkIngredientMapper, times(1)).mapToDrinkIngredientDto(drinkIngredient);
        verify(drinkIngredientMapper, times(1)).mapToDrinkIngredient(drinkIngredientDto);
    }
}
