package com.myapp.foodpairingbackend.mapper;

import com.myapp.foodpairingbackend.domain.dto.DrinkIngredientDto;
import com.myapp.foodpairingbackend.domain.entity.Drink;
import com.myapp.foodpairingbackend.domain.entity.DrinkIngredient;
import com.myapp.foodpairingbackend.service.DrinkService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
class DrinkIngredientMapperTest {

    @Autowired
    private DrinkIngredientMapper drinkIngredientMapper;

    @MockBean
    private DrinkService drinkService;

    @Test
    void testMapToDrinkIngredient() throws Exception {
        //Given
        Drink drink = Drink.builder().id(1L).externalSystemId("10").name("test name").alcoholic("test alcoholic")
                .glass("test glass").instructions("test instructions").drinkIngredientList(List.of())
                .build();
        DrinkIngredientDto drinkIngredientDto = DrinkIngredientDto.builder().id(2L).
                name("test name").measure("test measure").drinkId(1L)
                .build();
        when(drinkService.getDrink(drinkIngredientDto.getDrinkId())).thenReturn(drink);

        //When
        DrinkIngredient drinkIngredient = drinkIngredientMapper.mapToDrinkIngredient(drinkIngredientDto);

        //Then
        assertEquals(2L, drinkIngredient.getId());
        assertEquals("test name", drinkIngredient.getName());
        assertEquals("test measure", drinkIngredient.getMeasure());
        assertEquals(1L, drinkIngredient.getDrink().getId());
    }

    @Test
    void testMapToDrinkIngredientDto() throws Exception {
        //Given
        Drink drink = Drink.builder().id(1L).externalSystemId("10").name("test name").alcoholic("test alcoholic")
                .glass("test glass").instructions("test instructions").drinkIngredientList(List.of())
                .build();
        DrinkIngredient drinkIngredient = DrinkIngredient.builder().id(2L).
                name("test name").measure("test measure").drink(drink)
                .build();

        //When
        DrinkIngredientDto drinkIngredientDto = drinkIngredientMapper.mapToDrinkIngredientDto(drinkIngredient);

        //Then
        assertEquals(2L, drinkIngredientDto.getId());
        assertEquals("test name", drinkIngredientDto.getName());
        assertEquals("test measure", drinkIngredientDto.getMeasure());
        assertEquals(1L, drinkIngredientDto.getDrinkId());
    }

    @Test
    void testMapToDrinkIngredientList() throws Exception {
        //Given
        Drink drink = Drink.builder().id(1L).externalSystemId("10").name("test name").alcoholic("test alcoholic")
                .glass("test glass").instructions("test instructions").drinkIngredientList(List.of())
                .build();
        DrinkIngredientDto drinkIngredientDto = DrinkIngredientDto.builder().id(2L).
                name("test name").measure("test measure").drinkId(1L)
                .build();
        List<DrinkIngredientDto> drinkIngredientDtoList = List.of(drinkIngredientDto);
        when(drinkService.getDrink(drinkIngredientDto.getDrinkId())).thenReturn(drink);

        //When
        List<DrinkIngredient> drinkIngredientList =
                drinkIngredientMapper.mapToDrinkIngredientList(drinkIngredientDtoList);

        //Then
        assertEquals(1, drinkIngredientList.size());
        assertEquals(2L, drinkIngredientList.get(0).getId());
        assertEquals("test name", drinkIngredientList.get(0).getName());
        assertEquals("test measure", drinkIngredientList.get(0).getMeasure());
        assertEquals(1L, drinkIngredientList.get(0).getDrink().getId());
    }

    @Test
    void testMapToDrinkIngredientDtoList() throws Exception {
        //Given
        Drink drink = Drink.builder().id(1L).externalSystemId("10").name("test name").alcoholic("test alcoholic")
                .glass("test glass").instructions("test instructions").drinkIngredientList(List.of())
                .build();
        DrinkIngredient drinkIngredient = DrinkIngredient.builder().id(2L).
                name("test name").measure("test measure").drink(drink)
                .build();
        List<DrinkIngredient> drinkIngredientList = List.of(drinkIngredient);

        //When
        List<DrinkIngredientDto> drinkIngredientDtoList =
                drinkIngredientMapper.mapToDrinkIngredientDtoList(drinkIngredientList);

        //Then
        assertEquals(1, drinkIngredientDtoList.size());
        assertEquals(2L, drinkIngredientDtoList.get(0).getId());
        assertEquals("test name", drinkIngredientDtoList.get(0).getName());
        assertEquals("test measure", drinkIngredientDtoList.get(0).getMeasure());
        assertEquals(1L, drinkIngredientDtoList.get(0).getDrinkId());
    }
}