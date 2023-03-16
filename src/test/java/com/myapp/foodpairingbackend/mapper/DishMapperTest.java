package com.myapp.foodpairingbackend.mapper;

import com.myapp.foodpairingbackend.domain.dto.DishDto;
import com.myapp.foodpairingbackend.domain.entity.Dish;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class DishMapperTest {

    @Autowired
    private DishMapper dishMapper;

    @Test
    void testMapToDish() throws Exception {
        //Given
        DishDto dishDto = DishDto.builder().id(1L).externalSystemId(10L).name("test name").readyInMinutes(15)
                .servings(4).recipeUrl("https://test.com").compositionList(List.of())
                .build();

        //When
        Dish dish = dishMapper.mapToDish(dishDto);

        //Then
        assertEquals(1L, dish.getId());
        assertEquals(10L, dish.getExternalSystemId());
        assertEquals("test name", dish.getName());
        assertEquals(15, dish.getReadyInMinutes());
        assertEquals(4, dish.getServings());
        assertEquals("https://test.com", dish.getRecipeUrl());
        assertEquals(0, dish.getCompositionList().size());
    }

    @Test
    void testMapToDishDto() {
        //Given
        Dish dish = Dish.builder().id(1L).externalSystemId(10L).name("test name").readyInMinutes(15)
                .servings(4).recipeUrl("https://test.com").compositionList(List.of())
                .build();

        //When
        DishDto dishDto = dishMapper.mapToDishDto(dish);

        //Then
        assertEquals(1L, dishDto.getId());
        assertEquals(10L, dishDto.getExternalSystemId());
        assertEquals("test name", dishDto.getName());
        assertEquals(15, dishDto.getReadyInMinutes());
        assertEquals(4, dishDto.getServings());
        assertEquals("https://test.com", dishDto.getRecipeUrl());
        assertEquals(0, dishDto.getCompositionList().size());
    }

    @Test
    void testMapToDishDtoList() {
        //Given
        Dish dish = Dish.builder().id(1L).externalSystemId(10L).name("test name").readyInMinutes(15)
                .servings(4).recipeUrl("https://test.com").compositionList(List.of())
                .build();
        List<Dish> dishList = List.of(dish);

        //When
        List<DishDto> dishDtoList = dishMapper.mapToDishDtoList(dishList);

        //Then
        assertEquals(1, dishDtoList.size());
        assertEquals(1L, dishDtoList.get(0).getId());
        assertEquals(10L, dishDtoList.get(0).getExternalSystemId());
        assertEquals("test name", dishDtoList.get(0).getName());
        assertEquals(15, dishDtoList.get(0).getReadyInMinutes());
        assertEquals(4, dishDtoList.get(0).getServings());
        assertEquals("https://test.com", dishDtoList.get(0).getRecipeUrl());
        assertEquals(0, dishDtoList.get(0).getCompositionList().size());
    }

}