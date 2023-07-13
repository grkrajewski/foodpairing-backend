package com.myapp.foodpairingbackend.facade;

import com.myapp.foodpairingbackend.domain.dto.DishDto;
import com.myapp.foodpairingbackend.domain.entity.Dish;
import com.myapp.foodpairingbackend.exception.ComponentExistsException;
import com.myapp.foodpairingbackend.exception.ComponentNotFoundException;
import com.myapp.foodpairingbackend.exception.IdException;
import com.myapp.foodpairingbackend.mapper.DishMapper;
import com.myapp.foodpairingbackend.service.DishService;
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
public class DishFacadeTest {

    @Autowired
    DishFacade dishFacade;

    @MockBean
    private DishService dishService;

    @SpyBean
    private DishMapper dishMapper;

    private Dish dish;
    private DishDto dishDto;

    @BeforeEach
    void setup() {
        //Given
        dish = Dish.builder()
                .id(1L).externalSystemId(10L).name("test name dish").readyInMinutes(10)
                .servings(4).recipeUrl("https://test.com").compositionList(List.of())
                .build();

        dishDto = DishDto.builder()
                .id(1L).externalSystemId(10L).name("test name dish").readyInMinutes(10)
                .servings(4).recipeUrl("https://test.com").compositionList(List.of())
                .build();
    }

    @Test
    void testGetDishes() {
        //Given
        when(dishService.getDishes()).thenReturn(List.of(dish));

        //When
        List<DishDto> dishes = dishFacade.getDishes();

        //Then
        assertEquals(1, dishes.size());
        verify(dishService, times(1)).getDishes();
        verify(dishMapper, times(1)).mapToDishDtoList(List.of(dish));
    }

    @Test
    void testGetDish() throws ComponentNotFoundException {
        //Give
        when(dishService.getDish(dish.getId())).thenReturn(dish);

        //When
        DishDto fetchedDishDto = dishFacade.getDish(dish.getId());

        //Then
        assertEquals(1L, fetchedDishDto.getId());
        assertEquals(10L, fetchedDishDto.getExternalSystemId());
        assertEquals("test name dish", fetchedDishDto.getName());
        assertEquals(10, fetchedDishDto.getReadyInMinutes());
        assertEquals(4, fetchedDishDto.getServings());
        assertEquals("https://test.com", fetchedDishDto.getRecipeUrl());
        assertTrue(fetchedDishDto.getCompositionList().isEmpty());
        verify(dishService, times(1)).getDish(1L);
        verify(dishMapper, times(1)).mapToDishDto(dish);
    }

    @Test
    void testDeleteDish() throws ComponentNotFoundException {
        //Given
        doNothing().when(dishService).deleteDish(dish.getId());

        //When
        dishFacade.deleteDish(dish.getId());

        //Then
        verify(dishService, times(1)).deleteDish(1L);
    }

    @Test
    void testSaveDishInDb() throws ComponentExistsException, IdException, ComponentNotFoundException {
        //Given
        when(dishService.saveDish(any(Dish.class))).thenReturn(dish);

        //When
        DishDto savedDishDto = dishFacade.saveDishInDb(dishDto);

        //Then
        assertEquals(1L, savedDishDto.getId());
        assertEquals(10L, savedDishDto.getExternalSystemId());
        assertEquals("test name dish", savedDishDto.getName());
        assertEquals(10, savedDishDto.getReadyInMinutes());
        assertEquals(4, savedDishDto.getServings());
        assertEquals("https://test.com", savedDishDto.getRecipeUrl());
        assertTrue(savedDishDto.getCompositionList().isEmpty());
        verify(dishService, times(1)).saveDish(any(Dish.class));
        verify(dishMapper, times(1)).mapToDish(dishDto);
        verify(dishMapper, times(1)).mapToDishDto(dish);
    }

    @Test
    void testUpdateDish() throws ComponentNotFoundException, IdException {
        //Give
        when(dishService.updateDish(any(Dish.class))).thenAnswer(answer -> {
            ReflectionTestUtils.setField(dish, "name", "test updated name");
            return dish;
        });

        //When
        DishDto updatedDishDto = dishFacade.updateDish(dishDto);

        //Then
        assertEquals("test updated name", updatedDishDto.getName());
        verify(dishService, times(1)).updateDish(any(Dish.class));
        verify(dishMapper, times(1)).mapToDish(dishDto);
        verify(dishMapper, times(1)).mapToDishDto(dish);
    }
}
