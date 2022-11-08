package com.myapp.foodpairingbackend.service;

import com.myapp.foodpairingbackend.domain.entity.Dish;
import com.myapp.foodpairingbackend.exception.CompositionNotFoundException;
import com.myapp.foodpairingbackend.exception.DishNotFoundException;
import com.myapp.foodpairingbackend.repository.DishRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
@SpringBootTest
class DishServiceTest {

    @Autowired
    private DishService dishService;

    @Autowired
    private DishRepository dishRepository;

    @Test
    void testGetDish() throws CompositionNotFoundException, DishNotFoundException {
        //Given
        Dish dish = Dish.builder()
                .id(null)
                .externalSystemId(1L).name("test name dish").readyInMinutes(10).servings(4)
                .recipeUrl("https://test.com").compositionList(new ArrayList<>())
                .build();

        dishService.saveDish(dish);
        Long dishId = dish.getId();

        //When
        Dish savedDish = dishService.getDish(dishId);

        //Then
        assertTrue(dishRepository.existsById(dishId));
        assertEquals("test name dish", savedDish.getName());
    }

    @Test
    void testDeleteDish() {
        //Given
        Dish dish = Dish.builder()
                .id(null)
                .externalSystemId(1L).name("test name dish").readyInMinutes(10).servings(4)
                .recipeUrl("https://test.com").compositionList(new ArrayList<>())
                .build();

        dishService.saveDish(dish);
        Long dishId = dish.getId();

        //When
        dishService.deleteDish(dishId);

        //Then
        assertFalse(dishRepository.existsById(dishId));
    }
}