package com.myapp.foodpairingbackend.service;

import com.myapp.foodpairingbackend.domain.entity.Composition;
import com.myapp.foodpairingbackend.domain.entity.Dish;
import com.myapp.foodpairingbackend.domain.entity.Drink;
import com.myapp.foodpairingbackend.exception.ComponentExistsException;
import com.myapp.foodpairingbackend.exception.ComponentNotFoundException;
import com.myapp.foodpairingbackend.repository.CompositionRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
@SpringBootTest
class CompositionServiceTest {

    @Autowired
    private CompositionService compositionService;

    @Autowired
    private CompositionRepository compositionRepository;

    //Given - data preparation
    Dish dish = Dish.builder()
            .id(null).externalSystemId(1L).name("test name dish").readyInMinutes(10).servings(4)
            .recipeUrl("https://test.com").compositionList(List.of())
            .build();

    Drink drink = Drink.builder()
            .id(null).externalSystemId("2").name("test name drink").alcoholic("test alcoholic")
            .glass("test glass").instructions("test instructions").drinkIngredientList(List.of())
            .build();

    Composition composition = Composition.builder()
            .id(null).dish(dish).drink(drink).created(new Date()).commentList(List.of())
            .build();

    @Test
    void testGetComposition() throws ComponentNotFoundException, ComponentExistsException {
        //Given
        compositionService.saveComposition(composition);
        Long compositionId = composition.getId();

        //When
        Composition savedComposition = compositionService.getComposition(compositionId);

        //Then
        assertTrue(compositionRepository.existsById(compositionId));
        assertEquals("test name dish", savedComposition.getDish().getName());
        assertEquals("test name drink", savedComposition.getDrink().getName());
        assertNotNull(savedComposition.getCreated());
        assertEquals(0, savedComposition.getCommentList().size());
    }

    @Test
    void testDeleteComposition() throws ComponentExistsException {
        //Given
        compositionService.saveComposition(composition);
        Long compositionId = composition.getId();

        //When
        compositionService.deleteComposition(compositionId);

        //Then
        assertFalse(compositionRepository.existsById(compositionId));
    }

    @Test
    void testSaveComposition() throws ComponentExistsException {
        //When
        compositionService.saveComposition(composition);
        Long compositionId = composition.getId();

        //Then
        assertTrue(compositionRepository.existsById(compositionId));
    }
}