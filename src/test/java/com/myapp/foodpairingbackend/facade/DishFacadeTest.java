package com.myapp.foodpairingbackend.facade;

import com.myapp.foodpairingbackend.domain.dto.DishDto;
import com.myapp.foodpairingbackend.domain.entity.Dish;
import com.myapp.foodpairingbackend.mapper.DishMapper;
import com.myapp.foodpairingbackend.service.DishService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@SpringBootTest
public class DishFacadeTest {

    @Autowired
    DishFacade dishFacade;

    @MockBean
    private DishService dishService;

    @MockBean
    private DishMapper dishMapper;

    private List<Dish> dishes;
    private List<DishDto> dishesDto;

    @BeforeEach
    void setup() {
        //Given
        Dish dish = Dish.builder()
                .id(1L).externalSystemId(10L).name("test name dish").readyInMinutes(10)
                .servings(4).recipeUrl("https://test.com").compositionList(List.of())
                .build();
        DishDto dishDto = DishDto.builder()
                .id(1L).externalSystemId(10L).name("test name dishDto").readyInMinutes(10)
                .servings(4).recipeUrl("https://testDto.com").compositionList(List.of())
                .build();
        dishes = List.of(dish);
        dishesDto = List.of(dishDto);
    }

    @Test
    void testGetDishes() {
        //Given
        when(dishService.getDishes()).thenReturn(dishes);
        when(dishMapper.mapToDishDtoList(dishes)).thenReturn(dishesDto);

        //When
        List<DishDto> dishes = dishFacade.getDishes();

        //Then
        assertEquals(dishes.size(), 1);
        assertEquals(1L, dishes.get(0).getId());
        assertEquals(10L, dishes.get(0).getExternalSystemId());
        assertEquals("test name dishDto", dishes.get(0).getName());
        assertEquals(10, dishes.get(0).getReadyInMinutes());
        assertEquals(4, dishes.get(0).getServings());
        assertEquals("https://testDto.com", dishes.get(0).getRecipeUrl());
        assertEquals(0, dishes.get(0).getCompositionList().size());
    }

    @Test
    void testGetEmptyDishes() {
        //Given
        when(dishService.getDishes()).thenReturn(List.of());

        //When
        List<DishDto> dishes = dishFacade.getDishes();

        //Then
        assertTrue(dishes.isEmpty());
    }
}
