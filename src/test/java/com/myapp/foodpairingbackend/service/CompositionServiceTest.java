package com.myapp.foodpairingbackend.service;

import com.myapp.foodpairingbackend.domain.entity.Composition;
import com.myapp.foodpairingbackend.domain.entity.Dish;
import com.myapp.foodpairingbackend.domain.entity.Drink;
import com.myapp.foodpairingbackend.exception.ComponentExistsException;
import com.myapp.foodpairingbackend.exception.ComponentNotFoundException;
import com.myapp.foodpairingbackend.exception.IdException;
import com.myapp.foodpairingbackend.repository.CompositionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.util.ReflectionTestUtils;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class CompositionServiceTest {

    @Autowired
    private CompositionService compositionService;

    @MockBean
    private CompositionRepository compositionRepository;

    private Composition composition;

    @BeforeEach
    void setup() {
        //Given
        Dish dish = Dish.builder()
                .id(1L).externalSystemId(10L).name("test name dish").readyInMinutes(10).servings(4)
                .recipeUrl("https://test.com").compositionList(List.of())
                .build();

        Drink drink = Drink.builder()
                .id(2L).externalSystemId("20").name("test name drink").alcoholic("test alcoholic")
                .glass("test glass").instructions("test instructions").drinkIngredientList(List.of())
                .build();

        composition = Composition.builder()
                .id(3L).dish(dish).drink(drink).created(LocalDateTime.now()).commentList(List.of())
                .build();
    }

    @Test
    void testGetCompositions() {
        //Given
        when(compositionRepository.findAll()).thenReturn(List.of(composition));

        //When
        List<Composition> compositions = compositionService.getCompositions();

        //Then
        assertEquals(1, compositions.size());
        verify(compositionRepository, times(1)).findAll();
    }

    @Test
    void testGetCompositions_ShouldFetchEmptyList() {
        //Given
        when(compositionRepository.findAll()).thenReturn(List.of());

        //When
        List<Composition> compositions = compositionService.getCompositions();

        //Then
        assertEquals(0, compositions.size());
        verify(compositionRepository, times(1)).findAll();
    }

    @Test
    void testGetComposition() throws ComponentNotFoundException {
        //Given
        when(compositionRepository.findById(composition.getId())).thenReturn(Optional.ofNullable(composition));

        //When
        Composition savedComposition = compositionService.getComposition(composition.getId());

        //Then
        assertEquals(3L, savedComposition.getId());
        assertNotNull(composition.getDish());
        assertNotNull(composition.getDrink());
        assertNotNull(savedComposition.getCreated());
        assertEquals(0, savedComposition.getCommentList().size());
        verify(compositionRepository, times(1)).findById(3L);
    }

    @Test
    void testDeleteComposition() throws ComponentNotFoundException {
        //Given
        doNothing().when(compositionRepository).deleteById(composition.getId());

        //When
        compositionService.deleteComposition(composition.getId());

        //Then
        verify(compositionRepository, times(1)).deleteById(3L);
    }

    @Test
    void testSaveComposition() throws ComponentExistsException, IdException {
        //Given
        ReflectionTestUtils.setField(composition, "id", null);
        when(compositionRepository.save(composition)).thenAnswer(answer -> {
            ReflectionTestUtils.setField(composition, "id", 3L);
            return composition;
        });

        //When
        Composition savedComposition = compositionService.saveComposition(composition);

        //Then
        assertEquals(3L, savedComposition.getId());
        assertNotNull(composition.getDish());
        assertNotNull(composition.getDrink());
        assertNotNull(savedComposition.getCreated());
        assertEquals(0, savedComposition.getCommentList().size());
        verify(compositionRepository, times(1)).save(composition);
    }

    @Test
    void testSaveComposition_ShouldThrowIdException() {
        //When & Then
        assertThrows(IdException.class, () -> compositionService.saveComposition(composition));
    }

    @Test
    void testSaveComposition_ShouldThrowComponentExistsException() {
        //Given
        ReflectionTestUtils.setField(composition, "id", null);
        when(compositionRepository.findByDrinkId(composition.getDrink().getId())).thenAnswer(answer -> {
            ReflectionTestUtils.setField(composition, "id", 3L);
            return composition;
        });

        //When & Then
        assertThrows(ComponentExistsException.class, () -> compositionService.saveComposition(composition));
    }

    @Test
    void testUpdateComposition() throws ComponentExistsException, IdException, ComponentNotFoundException {
        //Given
        when(compositionRepository.findById(composition.getId())).thenReturn(Optional.ofNullable(composition));
        when(compositionRepository.save(composition)).thenAnswer(answer -> {
            ReflectionTestUtils.setField(composition, "created", LocalDateTime.of(2023, 5, 22, 13, 30, 15));
            return composition;
        });

        //When
        Composition updatedComposition = compositionService.updateComposition(composition);

        //Then
        assertEquals(LocalDateTime.of(2023, 5, 22, 13, 30, 15), updatedComposition.getCreated());
        verify(compositionRepository, times(1)).save(composition);
    }

    @Test
    void testUpdateComposition_ShouldThrowIdException() {
        //Give
        ReflectionTestUtils.setField(composition, "id",  null);

        //When & Then
        assertThrows(IdException.class, () -> compositionService.updateComposition(composition));
    }

    @Test
    void testUpdateComposition_ShouldThrowComponentExistsException() {
        //Given
        when(compositionRepository.findById(composition.getId())).thenReturn(Optional.ofNullable(composition));
        when(compositionRepository.findByDrinkId(composition.getDrink().getId())).thenReturn(composition);

        //When & Then
        assertThrows(ComponentExistsException.class, () -> compositionService.updateComposition(composition));
    }
}