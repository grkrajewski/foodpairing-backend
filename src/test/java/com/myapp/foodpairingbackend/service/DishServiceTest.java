package com.myapp.foodpairingbackend.service;

import com.myapp.foodpairingbackend.domain.entity.Dish;
import com.myapp.foodpairingbackend.exception.ComponentExistsException;
import com.myapp.foodpairingbackend.exception.ComponentNotFoundException;
import com.myapp.foodpairingbackend.exception.IdException;
import com.myapp.foodpairingbackend.repository.DishRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@Transactional
@SpringBootTest
class DishServiceTest {

    @Autowired
    private DishService dishService;

    @Autowired
    private DishRepository dishRepository;

    @InjectMocks
    private DishService dishServiceMock;

    @Mock
    private DishRepository dishRepositoryMock;

    //Given - data preparation
    Dish dish = Dish.builder()
            .id(null)
            .externalSystemId(1L).name("test name dish").readyInMinutes(10).servings(4)
            .recipeUrl("https://test.com").compositionList(List.of())
            .build();

    @Test
    void testGetDishes() {
        //Given
        when(dishRepositoryMock.findAll()).thenReturn(List.of(dish));

        //When
        List<Dish> dishes = dishServiceMock.getDishes();

        //Then
        assertEquals(1, dishes.size());
        verify(dishRepositoryMock, times(1)).findAll();
    }

    @Test
    void testGetDishes_ShouldFetchEmptyList() {
        //When
        List<Dish> dishes = dishServiceMock.getDishes();

        //Then
        assertEquals(0, dishes.size());
        verify(dishRepositoryMock, times(1)).findAll();
    }

    @Test
    void testGetDish() throws ComponentNotFoundException, ComponentExistsException, IdException {
        //Given
        dishService.saveDish(dish);
        Long dishId = dish.getId();

        //When
        Dish savedDish = dishService.getDish(dishId);

        //Then
        assertTrue(dishRepository.existsById(dishId));
        assertEquals(1L, savedDish.getExternalSystemId());
        assertEquals("test name dish", savedDish.getName());
        assertEquals(10, savedDish.getReadyInMinutes());
        assertEquals(4, savedDish.getServings());
        assertEquals("https://test.com", savedDish.getRecipeUrl());
        assertEquals(0, savedDish.getCompositionList().size());
    }

    @Test
    void testGetDishByExternalSystemId() throws ComponentExistsException, IdException {
        //Given
        dishService.saveDish(dish);
        Long dishId = dish.getId();
        Long externalSystemId = dish.getExternalSystemId();

        //When
        Dish savedDish = dishService.getDishByExternalSystemId(externalSystemId);

        //Then
        assertTrue(dishRepository.existsById(dishId));
        assertEquals(1L, savedDish.getExternalSystemId());
        assertEquals("test name dish", savedDish.getName());
        assertEquals(10, savedDish.getReadyInMinutes());
        assertEquals(4, savedDish.getServings());
        assertEquals("https://test.com", savedDish.getRecipeUrl());
        assertEquals(0, savedDish.getCompositionList().size());
    }

    @Test
    void testDeleteDish() throws ComponentNotFoundException, ComponentExistsException, IdException {
        //Given
        dishService.saveDish(dish);
        Long dishId = dish.getId();

        //When
        dishService.deleteDish(dishId);

        //Then
        assertFalse(dishRepository.existsById(dishId));
    }

    @Test
    void testDeleteDish_ShouldThrowComponentNotFoundException() {
        //When & Then
        assertThrows(ComponentNotFoundException.class, () -> dishService.deleteDish(1L));
    }

    @Test
    void testSaveDish() throws ComponentExistsException, IdException {
        //When
        dishService.saveDish(dish);
        Long dishId = dish.getId();

        //Then
        assertTrue(dishRepository.existsById(dishId));
    }

    @Test
    void testSaveDish_ShouldThrowIdException() {
        //Given
        Dish dishWithId = Dish.builder()
                .id(1L)
                .externalSystemId(1L).name("test name dish").readyInMinutes(10).servings(4)
                .recipeUrl("https://test.com").compositionList(List.of())
                .build();

        //When & Then
        assertThrows(IdException.class, () -> dishService.saveDish(dishWithId));
    }

    @Test
    void testUpdateDish() throws ComponentExistsException, IdException, ComponentNotFoundException {
        //Given
        dishService.saveDish(dish);
        Long dishId = dish.getId();
        Dish nameUpdatedDish = Dish.builder()
                .id(dishId)
                .externalSystemId(1L).name("test updated name dish").readyInMinutes(10).servings(4)
                .recipeUrl("https://test.com").compositionList(List.of())
                .build();

        //When
        Dish updatedDish = dishService.updateDish(nameUpdatedDish);

        //Then
        assertEquals("test updated name dish", updatedDish.getName());
    }

    @Test
    void testUpdateDish_ShouldThrowIdException() {
        //When & Then
        assertThrows(IdException.class, () -> dishService.updateDish(dish));
    }

}