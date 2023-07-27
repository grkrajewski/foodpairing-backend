package com.myapp.foodpairingbackend.facade;

import com.myapp.foodpairingbackend.domain.dto.DrinkDto;
import com.myapp.foodpairingbackend.domain.entity.Drink;
import com.myapp.foodpairingbackend.exception.ComponentNotFoundException;
import com.myapp.foodpairingbackend.exception.IdException;
import com.myapp.foodpairingbackend.mapper.DrinkMapper;
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
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@SpringBootTest
public class DrinkFacadeTest {

    @Autowired
    private DrinkFacade drinkFacade;

    @MockBean
    private DrinkService drinkService;

    @SpyBean
    private DrinkMapper drinkMapper;

    private Drink drink;
    private DrinkDto drinkDto;

    @BeforeEach
    void setup() {
        drink = Drink.builder()
                .id(1L).externalSystemId("20").name("test name drink").alcoholic("test alcoholic")
                .glass("test glass").instructions("test instructions").drinkIngredientList(List.of())
                .build();

        drinkDto = DrinkDto.builder()
                .id(1L).externalSystemId("20").name("test name drink").alcoholic("test alcoholic")
                .glass("test glass").instructions("test instructions").drinkIngredientList(List.of())
                .build();
    }

    @Test
    void testGetDrinks() {
        //Given
        when(drinkService.getDrinks()).thenReturn(List.of(drink));

        //When
        List<DrinkDto> drinks = drinkFacade.getDrinks();

        //Then
        assertEquals(1, drinks.size());
        verify(drinkService, times(1)).getDrinks();
        verify(drinkMapper, times(1)).mapToDrinkDtoList(List.of(drink));
    }

    @Test
    void testGetDrink() throws ComponentNotFoundException {
        //Given
        when(drinkService.getDrink(drink.getId())).thenReturn(drink);

        //When
        DrinkDto fetchedDrinkDto = drinkFacade.getDrink(drinkDto.getId());

        //Then
        assertEquals(1L, fetchedDrinkDto.getId());
        assertEquals("20", fetchedDrinkDto.getExternalSystemId());
        assertEquals("test name drink", fetchedDrinkDto.getName());
        assertEquals("test alcoholic", fetchedDrinkDto.getAlcoholic());
        assertEquals("test glass", fetchedDrinkDto.getGlass());
        assertEquals("test instructions", fetchedDrinkDto.getInstructions());
        assertTrue(fetchedDrinkDto.getDrinkIngredientList().isEmpty());
        verify(drinkService, times(1)).getDrink(1L);
        verify(drinkMapper, times(1)).mapToDrinkDto(drink);
    }

    @Test
    void testDeleteDrink() throws ComponentNotFoundException {
        //Given
        doNothing().when(drinkService).deleteDrink(drink.getId());

        //When
        drinkFacade.deleteDrink(drink.getId());

        //Then
        verify(drinkService, times(1)).deleteDrink(1L);
    }

    @Test
    void testSaveDrinkInDb() throws IdException, ComponentNotFoundException {
        //Given
        when(drinkService.saveDrink(any(Drink.class))).thenReturn(drink);

        //When
        DrinkDto savedDrinkDto = drinkFacade.saveDrinkInDb(drinkDto);

        //Then
        assertEquals(1L, savedDrinkDto.getId());
        assertEquals("20", savedDrinkDto.getExternalSystemId());
        assertEquals("test name drink", savedDrinkDto.getName());
        assertEquals("test alcoholic", savedDrinkDto.getAlcoholic());
        assertEquals("test glass", savedDrinkDto.getGlass());
        assertEquals("test instructions", savedDrinkDto.getInstructions());
        assertTrue(savedDrinkDto.getDrinkIngredientList().isEmpty());
        verify(drinkService, times(1)).saveDrink(any(Drink.class));
        verify(drinkMapper, times(1)).mapToDrinkDto(drink);
        verify(drinkMapper, times(1)).mapToDrink(drinkDto);
    }

    @Test
    void testUpdateDrink() throws ComponentNotFoundException, IdException {
        //Given
        when(drinkService.updateDrink(any(Drink.class))).thenAnswer(answer -> {
            ReflectionTestUtils.setField(drink, "name", "test updated name drink");
            return drink;
        });

        //When
        DrinkDto updatedDrinkDto = drinkFacade.updateDrink(drinkDto);

        //Then
        assertEquals(1L, updatedDrinkDto.getId());
        assertEquals("20", updatedDrinkDto.getExternalSystemId());
        assertEquals("test updated name drink", updatedDrinkDto.getName());
        assertEquals("test alcoholic", updatedDrinkDto.getAlcoholic());
        assertEquals("test glass", updatedDrinkDto.getGlass());
        assertEquals("test instructions", updatedDrinkDto.getInstructions());
        assertTrue(updatedDrinkDto.getDrinkIngredientList().isEmpty());
        verify(drinkService, times(1)).updateDrink(any(Drink.class));
        verify(drinkMapper, times(1)).mapToDrinkDto(drink);
        verify(drinkMapper, times(1)).mapToDrink(drinkDto);
    }
}
