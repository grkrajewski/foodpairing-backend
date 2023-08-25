package com.myapp.foodpairingbackend.service;

import com.myapp.foodpairingbackend.domain.entity.Composition;
import com.myapp.foodpairingbackend.domain.entity.Dish;
import com.myapp.foodpairingbackend.domain.entity.Drink;
import com.myapp.foodpairingbackend.exception.ComponentExistsException;
import com.myapp.foodpairingbackend.exception.ComponentNotFoundException;
import com.myapp.foodpairingbackend.exception.IdException;
import com.myapp.foodpairingbackend.repository.CompositionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Tag("integration-test")
@SpringBootTest
@Transactional
@TestPropertySource("/application-test.properties")
public class CompositionServiceDbIntegrationTest {

    @Autowired
    private CompositionService compositionService;

    @Autowired
    private CompositionRepository compositionRepository;

    private Composition composition;
    private Dish dish;
    private Drink drink;

    @BeforeEach
    void setup() {
        //Given
        dish = Dish.builder()
                .id(null).externalSystemId(10L).name("test name dish").readyInMinutes(12).servings(4)
                .recipeUrl("https://test.com").compositionList(List.of())
                .build();

        drink = Drink.builder()
                .id(null).externalSystemId("20").name("test name drink").alcoholic("test alcoholic")
                .glass("test glass").instructions("test instructions").drinkIngredientList(List.of())
                .build();

        composition = Composition.builder()
                .id(null).dish(dish).drink(drink).created(LocalDateTime.now()).commentList(List.of())
                .build();
    }

    @Test
    void testGetCompositions() throws ComponentExistsException, IdException {
        //Given
        compositionService.saveComposition(composition);

        //When
        List<Composition> compositions = compositionService.getCompositions();

        //Then
        assertEquals(1, compositions.size());
    }

    @Test
    void testGetCompositions_ShouldFetchEmptyList() {
        //When
        List<Composition> compositions = compositionService.getCompositions();

        //Then
        assertTrue(compositions.isEmpty());
    }

    @Test
    void testGetComposition() throws ComponentNotFoundException, ComponentExistsException, IdException {
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
        assertTrue(savedComposition.getCommentList().isEmpty());
    }

    @Test
    void testDeleteComposition() throws ComponentExistsException, ComponentNotFoundException, IdException {
        //Given
        compositionService.saveComposition(composition);
        Long compositionId = composition.getId();

        //When
        compositionService.deleteComposition(compositionId);

        //Then
        assertFalse(compositionRepository.existsById(compositionId));
    }

    @Test
    void testDeleteComposition_ShouldThrowComponentNotFoundException() {
        //When & Then
        assertThrows(ComponentNotFoundException.class, () -> compositionService.deleteComposition(1L));
    }

    @Test
    void testSaveComposition() throws ComponentExistsException, IdException {
        //When
        compositionService.saveComposition(composition);

        //Then
        assertTrue(compositionRepository.existsById(composition.getId()));
    }

    @Test
    void testSaveComposition_ShouldThrowIdException() {
        //Given
        ReflectionTestUtils.setField(composition, "id", 1L);

        //When & Then
        assertThrows(IdException.class, () -> compositionService.saveComposition(composition));
    }

    @Test
    void testUpdateComposition() throws ComponentExistsException, IdException, ComponentNotFoundException {
        //Given
        compositionService.saveComposition(composition);
        Drink updatedDrink = Drink.builder()
                .id(null).externalSystemId("20").name("test updated name drink").alcoholic("test alcoholic")
                .glass("test glass").instructions("test instructions").drinkIngredientList(List.of())
                .build();
        Composition drinkUpdatedComposition = Composition.builder()
                .id(composition.getId()).dish(dish).drink(updatedDrink).created(LocalDateTime.now()).commentList(List.of())
                .build();

        //When
        compositionService.updateComposition(drinkUpdatedComposition);

        //Then
        assertEquals("test updated name drink", composition.getDrink().getName());
    }

    @Test
    void testUpdateComposition_ShouldThrowIdException() {
        //When & Then
        assertThrows(IdException.class, () -> compositionService.updateComposition(composition));
    }
}
