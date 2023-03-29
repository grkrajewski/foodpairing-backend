package com.myapp.foodpairingbackend.mapper;

import com.myapp.foodpairingbackend.domain.dto.DrinkDto;
import com.myapp.foodpairingbackend.domain.entity.Drink;
import com.myapp.foodpairingbackend.exception.ComponentNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class DrinkMapperTest {

    @Autowired
    private DrinkMapper drinkMapper;

    @Test
    void testMapToDrink() throws ComponentNotFoundException {
        //Given
        DrinkDto drinkDto = DrinkDto.builder().id(1L).externalSystemId("10").name("test name").alcoholic("test alcoholic")
                .glass("test glass").instructions("test instructions").drinkIngredientList(List.of())
                .build();

        //When
        Drink drink = drinkMapper.mapToDrink(drinkDto);

        //Then
        assertEquals(1L, drink.getId());
        assertEquals("10", drink.getExternalSystemId());
        assertEquals("test name", drink.getName());
        assertEquals("test alcoholic", drink.getAlcoholic());
        assertEquals("test glass", drink.getGlass());
        assertEquals("test instructions", drink.getInstructions());
        assertEquals(0, drink.getDrinkIngredientList().size());
    }

    @Test
    void testMapToDrinkDto() {
        //Given
        Drink drink = Drink.builder().id(1L).externalSystemId("10").name("test name").alcoholic("test alcoholic")
                .glass("test glass").instructions("test instructions").drinkIngredientList(List.of())
                .build();

        //When
        DrinkDto drinkDto = drinkMapper.mapToDrinkDto(drink);

        //Then
        assertEquals(1L, drinkDto.getId());
        assertEquals("10", drinkDto.getExternalSystemId());
        assertEquals("test name", drinkDto.getName());
        assertEquals("test alcoholic", drinkDto.getAlcoholic());
        assertEquals("test glass", drinkDto.getGlass());
        assertEquals("test instructions", drinkDto.getInstructions());
        assertEquals(0, drinkDto.getDrinkIngredientList().size());
    }

    @Test
    void testMapToDrinkDtoList() {
        //Given
        Drink drink = Drink.builder().id(1L).externalSystemId("10").name("test name").alcoholic("test alcoholic")
                .glass("test glass").instructions("test instructions").drinkIngredientList(List.of())
                .build();
        List<Drink> drinkList = List.of(drink);

        //When
        List<DrinkDto> drinkDtoList = drinkMapper.mapToDrinkDtoList(drinkList);

        //Then
        assertEquals(1, drinkDtoList.size());
        assertEquals(1L, drinkDtoList.get(0).getId());
        assertEquals("10", drinkDtoList.get(0).getExternalSystemId());
        assertEquals("test name", drinkDtoList.get(0).getName());
        assertEquals("test alcoholic", drinkDtoList.get(0).getAlcoholic());
        assertEquals("test glass", drinkDtoList.get(0).getGlass());
        assertEquals("test instructions", drinkDtoList.get(0).getInstructions());
        assertEquals(0, drinkDtoList.get(0).getDrinkIngredientList().size());
    }
}