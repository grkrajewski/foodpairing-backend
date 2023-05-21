package com.myapp.foodpairingbackend.service;

import com.myapp.foodpairingbackend.domain.entity.Dish;
import com.myapp.foodpairingbackend.exception.ComponentExistsException;
import com.myapp.foodpairingbackend.exception.ComponentNotFoundException;
import com.myapp.foodpairingbackend.exception.IdException;
import com.myapp.foodpairingbackend.repository.DishRepository;
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
class DishServiceTest {

    @Autowired
    private DishService dishService;

    @MockBean
    private DishRepository dishRepository;

    private Dish dish;

    @BeforeEach
    void setup() {
        //Given
        dish = Dish.builder()
                .id(1L).externalSystemId(10L).name("test name dish").readyInMinutes(10)
                .servings(4).recipeUrl("https://test.com").compositionList(List.of())
                .build();
    }

    @Test
    void testGetDishes() {
        //Given
        when(dishRepository.findAll()).thenReturn(List.of(dish));

        //When
        List<Dish> dishes = dishService.getDishes();

        //Then
        assertEquals(1, dishes.size());
        verify(dishRepository, times(1)).findAll();
    }

    @Test
    void testGetDishes_ShouldFetchEmptyList() {
        //Given
        when(dishRepository.findAll()).thenReturn(List.of());

        //When
        List<Dish> dishes = dishService.getDishes();

        //Then
        assertEquals(0, dishes.size());
        verify(dishRepository, times(1)).findAll();
    }

    @Test
    void testGetDish() throws ComponentNotFoundException {
        //Given
        when(dishRepository.findById(dish.getId())).thenReturn(Optional.ofNullable(dish));

        //When
        Dish savedDish = dishService.getDish(dish.getId());

        //Then
        assertEquals(1L, savedDish.getId());
        assertEquals(10L, savedDish.getExternalSystemId());
        assertEquals("test name dish", savedDish.getName());
        assertEquals(10, savedDish.getReadyInMinutes());
        assertEquals(4, savedDish.getServings());
        assertEquals("https://test.com", savedDish.getRecipeUrl());
        assertEquals(0, savedDish.getCompositionList().size());
        verify(dishRepository, times(1)).findById(1L);
    }

    @Test
    void testGetDishByExternalSystemId() {
        //Given
        when(dishRepository.findByExternalSystemId(dish.getExternalSystemId())).thenReturn(dish);

        //When
        Dish savedDish = dishService.getDishByExternalSystemId(dish.getExternalSystemId());

        //Then
        assertEquals(1L, savedDish.getId());
        assertEquals(10L, savedDish.getExternalSystemId());
        assertEquals("test name dish", savedDish.getName());
        assertEquals(10, savedDish.getReadyInMinutes());
        assertEquals(4, savedDish.getServings());
        assertEquals("https://test.com", savedDish.getRecipeUrl());
        assertEquals(0, savedDish.getCompositionList().size());
        verify(dishRepository, times(1)).findByExternalSystemId(10L);
    }

    @Test
    void testGetDishByExternalSystemId_ShouldReturnNull() {
        //Given
        when(dishRepository.findByExternalSystemId(dish.getExternalSystemId())).thenReturn(null);

        //When
        Dish savedDish = dishService.getDishByExternalSystemId(dish.getExternalSystemId());

        //Then
        assertNull(savedDish);
        verify(dishRepository, times(1)).findByExternalSystemId(10L);
    }

    @Test
    void testDeleteDish() throws ComponentNotFoundException {
        //Given
        doNothing().when(dishRepository).deleteById(dish.getId());

        //When
        dishService.deleteDish(dish.getId());

        //Then
        verify(dishRepository, times(1)).deleteById(1L);
    }

    @Test
    void testSaveDish() throws ComponentExistsException, IdException {
        //Given
        ReflectionTestUtils.setField(dish, "id", null);
        when(dishRepository.save(any(Dish.class))).thenAnswer(answer -> {
            ReflectionTestUtils.setField(dish, "id", 1L);
            return dish;
        });

        //When
        Dish savedDish = dishService.saveDish(dish);

        //Then
        assertEquals(1L, savedDish.getId());
        assertEquals(10L, savedDish.getExternalSystemId());
        assertEquals("test name dish", savedDish.getName());
        assertEquals(10, savedDish.getReadyInMinutes());
        assertEquals(4, savedDish.getServings());
        assertEquals("https://test.com", savedDish.getRecipeUrl());
        assertEquals(0, savedDish.getCompositionList().size());
        verify(dishRepository, times(1)).save(dish);
    }

    @Test
    void testSaveDish_ShouldThrowIdException() {
        //When & Then
        assertThrows(IdException.class, () -> dishService.saveDish(dish));
    }

    @Test
    void testSaveDish_ShouldThrowComponentExistsException() {
        //Given
        ReflectionTestUtils.setField(dish, "id", null);
        when(dishRepository.findByExternalSystemId(dish.getExternalSystemId())).thenAnswer(answer -> {
            ReflectionTestUtils.setField(dish, "id", 1L);
            return dish;
        });

        //When & Then
        assertThrows(ComponentExistsException.class, () -> dishService.saveDish(dish));
    }

    @Test
    void testUpdateDish() throws IdException, ComponentNotFoundException {
        //Given
        when(dishRepository.findById(dish.getId())).thenReturn(Optional.ofNullable(dish));
        when(dishRepository.save(any(Dish.class))).thenAnswer(answer -> {
            ReflectionTestUtils.setField(dish, "name", "test updated name");
            return dish;
        });

        //When
        Dish updatedDish = dishService.updateDish(dish);

        //Then
        assertEquals("test updated name", updatedDish.getName());
    }

    @Test
    void testUpdateDish_ShouldThrowIdException() {
        //Given
        ReflectionTestUtils.setField(dish, "id", null);

        //When & Then
        assertThrows(IdException.class, () -> dishService.updateDish(dish));
    }
}