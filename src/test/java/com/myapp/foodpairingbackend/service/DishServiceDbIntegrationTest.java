package com.myapp.foodpairingbackend.service;

import com.myapp.foodpairingbackend.domain.entity.Dish;
import com.myapp.foodpairingbackend.exception.ComponentExistsException;
import com.myapp.foodpairingbackend.exception.ComponentNotFoundException;
import com.myapp.foodpairingbackend.exception.IdException;
import com.myapp.foodpairingbackend.repository.DishRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Tag("integration-test")
@SpringBootTest
@Transactional
@TestPropertySource("/application-test.properties")
public class DishServiceDbIntegrationTest {

    @Autowired
    private DishService dishService;

    @Autowired
    private DishRepository dishRepository;

    private Dish dish;

    @BeforeEach
    void setup() {
        //Given
        dish = Dish.builder()
                .id(null).externalSystemId(10L).name("test name dish").readyInMinutes(12)
                .servings(4).recipeUrl("https://test.com").compositionList(List.of())
                .build();
    }

    @Test
    void testGetDishes() throws ComponentExistsException, IdException {
        //Given
        dishService.saveDish(dish);

        //When
        List<Dish> dishes = dishService.getDishes();

        //Then
        assertEquals(1, dishes.size());
    }

    @Test
    void testGetDishes_ShouldFetchEmptyList() {
        //When
        List<Dish> dishes = dishService.getDishes();

        //Then
        assertTrue(dishes.isEmpty());
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
        assertEquals(10L, savedDish.getExternalSystemId());
        assertEquals("test name dish", savedDish.getName());
        assertEquals(12, savedDish.getReadyInMinutes());
        assertEquals(4, savedDish.getServings());
        assertEquals("https://test.com", savedDish.getRecipeUrl());
        assertTrue(savedDish.getCompositionList().isEmpty());
    }

    @Test
    void testGetDishByExternalSystemId() throws ComponentExistsException, IdException {
        //Given
        dishService.saveDish(dish);

        //When
        Dish savedDish = dishService.getDishByExternalSystemId(dish.getExternalSystemId());

        //Then
        assertTrue(dishRepository.existsById(dish.getId()));
        assertEquals(10L, savedDish.getExternalSystemId());
        assertEquals("test name dish", savedDish.getName());
        assertEquals(12, savedDish.getReadyInMinutes());
        assertEquals(4, savedDish.getServings());
        assertEquals("https://test.com", savedDish.getRecipeUrl());
        assertTrue(savedDish.getCompositionList().isEmpty());
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

        //Then
        assertTrue(dishRepository.existsById(dish.getId()));
    }

    @Test
    void testSaveDish_ShouldThrowIdException() {
        //Given
        ReflectionTestUtils.setField(dish, "id", 1L);

        //When & Then
        assertThrows(IdException.class, () -> dishService.saveDish(dish));
    }

    @Test
    void testUpdateDish() throws ComponentExistsException, IdException, ComponentNotFoundException {
        //Given
        dishService.saveDish(dish);
        Dish nameUpdatedDish = Dish.builder()
                .id(dish.getId())
                .externalSystemId(10L).name("test updated name dish").readyInMinutes(12).servings(4)
                .recipeUrl("https://test.com").compositionList(List.of())
                .build();

        //When
        dishService.updateDish(nameUpdatedDish);

        //Then
        assertEquals("test updated name dish", dish.getName());
    }

    @Test
    void testUpdateDish_ShouldThrowIdException() {
        //When & Then
        assertThrows(IdException.class, () -> dishService.updateDish(dish));
    }
}
