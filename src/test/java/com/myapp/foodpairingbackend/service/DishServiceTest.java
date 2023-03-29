package com.myapp.foodpairingbackend.service;

import com.myapp.foodpairingbackend.domain.entity.Dish;
import com.myapp.foodpairingbackend.exception.ComponentExistsException;
import com.myapp.foodpairingbackend.exception.ComponentNotFoundException;
import com.myapp.foodpairingbackend.exception.IdException;
import com.myapp.foodpairingbackend.repository.DishRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
@SpringBootTest
class DishServiceTest {

    @Autowired
    private DishService dishService;

    @Autowired
    private DishRepository dishRepository;

    //Given - data preparation
    Dish dish = Dish.builder()
            .id(null)
            .externalSystemId(1L).name("test name dish").readyInMinutes(10).servings(4)
            .recipeUrl("https://test.com").compositionList(List.of())
            .build();

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
    void testSaveDish() throws ComponentExistsException, IdException {
        //When
        dishService.saveDish(dish);
        Long dishId = dish.getId();

        //Then
        assertTrue(dishRepository.existsById(dishId));
    }
}