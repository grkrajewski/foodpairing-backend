package com.myapp.foodpairingbackend.service;

import com.myapp.foodpairingbackend.domain.entity.Composition;
import com.myapp.foodpairingbackend.domain.entity.Dish;
import com.myapp.foodpairingbackend.domain.entity.Drink;
import com.myapp.foodpairingbackend.exception.ComponentExistsException;
import com.myapp.foodpairingbackend.exception.ComponentNotFoundException;
import com.myapp.foodpairingbackend.exception.IdException;
import com.myapp.foodpairingbackend.repository.CompositionRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@Transactional
@SpringBootTest
class CompositionServiceTest {

    @Autowired
    private CompositionService compositionService;

    @Autowired
    private CompositionRepository compositionRepository;

    @InjectMocks
    private CompositionService compositionServiceMock;

    @Mock
    private CompositionRepository compositionRepositoryMock;

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
            .id(null).dish(dish).drink(drink).created(LocalDateTime.now()).commentList(List.of())
            .build();

    @Test
    void testGetCompositions() {
        //Given
        when(compositionRepositoryMock.findAll()).thenReturn(List.of(composition));

        //When
        List<Composition> compositions = compositionServiceMock.getCompositions();

        //Then
        assertEquals(1, compositions.size());
        verify(compositionRepositoryMock, times(1)).findAll();
    }

    @Test
    void testGetCompositions_ShouldFetchEmptyList() {
        //When
        List<Composition> compositions = compositionServiceMock.getCompositions();

        //Then
        assertEquals(0, compositions.size());
        verify(compositionRepositoryMock, times(1)).findAll();
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
        assertEquals(0, savedComposition.getCommentList().size());
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
        Long compositionId = composition.getId();

        //Then
        assertTrue(compositionRepository.existsById(compositionId));
    }

    @Test
    void testSaveComposition_ShouldThrowIdException() {
        //Given
        Composition compositionWithId = Composition.builder()
                .id(1L).dish(dish).drink(drink).created(LocalDateTime.now()).commentList(List.of())
                .build();

        //When & Then
        assertThrows(IdException.class, () -> compositionService.saveComposition(compositionWithId));
    }

    @Test
    void testUpdateComposition() throws ComponentExistsException, IdException, ComponentNotFoundException {
        //Given
        compositionService.saveComposition(composition);
        Long compositionId = composition.getId();
        Drink updatedDrink = Drink.builder()
                .id(null).externalSystemId("2").name("test updated name drink").alcoholic("test alcoholic")
                .glass("test glass").instructions("test instructions").drinkIngredientList(List.of())
                .build();
        Composition drinkUpdatedComposition = Composition.builder()
                .id(compositionId).dish(dish).drink(updatedDrink).created(LocalDateTime.now()).commentList(List.of())
                .build();

        //When
        Composition updatedComposition = compositionService.updateComposition(drinkUpdatedComposition);

        //Then
        assertEquals("test updated name drink", updatedComposition.getDrink().getName());
    }

    @Test
    void testUpdateComposition_ShouldThrowIdException() {
        //When & Then
        assertThrows(IdException.class, () -> compositionService.updateComposition(composition));
    }
}