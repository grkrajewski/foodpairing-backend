package com.myapp.foodpairingbackend.facade;

import com.myapp.foodpairingbackend.domain.dto.CompositionDto;
import com.myapp.foodpairingbackend.domain.entity.Composition;
import com.myapp.foodpairingbackend.domain.entity.Dish;
import com.myapp.foodpairingbackend.domain.entity.Drink;
import com.myapp.foodpairingbackend.exception.ComponentExistsException;
import com.myapp.foodpairingbackend.exception.ComponentNotFoundException;
import com.myapp.foodpairingbackend.exception.IdException;
import com.myapp.foodpairingbackend.mapper.CompositionMapper;
import com.myapp.foodpairingbackend.service.CompositionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class CompositionFacadeTest {

    @Autowired
    private CompositionFacade compositionFacade;

    @MockBean
    private CompositionService compositionService;

    @MockBean
    private CompositionMapper compositionMapper;

    private Composition composition;
    private CompositionDto compositionDto;

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

        compositionDto = CompositionDto.builder()
                .id(31L).dishId(11L).drinkId(21L).created("2023-07-10 13:12:33").commentList(List.of())
                .build();
    }

    @Test
    void testGetCompositions() {
        //Given
        when(compositionFacade.getCompositions()).thenReturn(List.of(compositionDto));
        when(compositionMapper.mapToCompositionDtoList(List.of(composition))).thenReturn(List.of(compositionDto));

        //When
        List<CompositionDto> compositions = compositionFacade.getCompositions();

        //Then
        assertEquals(1, compositions.size());
    }

    @Test
    void testGetComposition() throws ComponentNotFoundException {
        //Given
        when(compositionService.getComposition(composition.getId())).thenReturn(composition);
        when(compositionMapper.mapToCompositionDto(composition)).thenReturn(compositionDto);

        //When
        CompositionDto fetchedCompositionDto = compositionFacade.getComposition(composition.getId());

        //Then
        assertEquals(31L, fetchedCompositionDto.getId());
        assertEquals(11L, fetchedCompositionDto.getDishId());
        assertEquals(21L, fetchedCompositionDto.getDrinkId());
        assertEquals("2023-07-10 13:12:33", fetchedCompositionDto.getCreated());
        assertTrue(fetchedCompositionDto.getCommentList().isEmpty());
        verify(compositionService, times(1)).getComposition(3L);
        verify(compositionMapper, times(1)).mapToCompositionDto(composition);
    }

    @Test
    void testDeleteComposition() throws ComponentNotFoundException {
        //Given
        doNothing().when(compositionService).deleteComposition(composition.getId());

        //When
        compositionFacade.deleteComposition(composition.getId());

        //Then
        verify(compositionService, times(1)).deleteComposition(3L);
    }

    @Test
    void testSaveComposition() throws ComponentExistsException, IdException, ComponentNotFoundException {
        //Given
        when(compositionService.saveComposition(any(Composition.class))).thenReturn(composition);
        when(compositionMapper.mapToComposition(compositionDto)).thenReturn(composition);
        when(compositionMapper.mapToCompositionDto(composition)).thenReturn(compositionDto);

        //When
        CompositionDto savedCompositionDto = compositionFacade.saveComposition(compositionDto);

        //Then
        assertEquals(31L, savedCompositionDto.getId());
        assertEquals(11L, savedCompositionDto.getDishId());
        assertEquals(21L, savedCompositionDto.getDrinkId());
        assertEquals("2023-07-10 13:12:33", savedCompositionDto.getCreated());
        assertTrue(savedCompositionDto.getCommentList().isEmpty());
        verify(compositionService, times(1)).saveComposition(composition);
        verify(compositionMapper, times(1)).mapToCompositionDto(composition);
        verify(compositionMapper, times(1)).mapToComposition(compositionDto);
    }

    @Test
    void testUpdateComposition() throws ComponentExistsException, IdException, ComponentNotFoundException {
        //Given
        when(compositionService.updateComposition(any(Composition.class))).thenReturn(composition);
        when(compositionMapper.mapToComposition(compositionDto)).thenReturn(composition);
        when(compositionMapper.mapToCompositionDto(composition)).thenAnswer(answer -> {
            ReflectionTestUtils.setField(compositionDto, "created", "2023-07-11 10:20:30");
            return compositionDto;
        });

        //When
        CompositionDto updatedCompositionDto = compositionFacade.updateComposition(compositionDto);

        //Then
        assertEquals("2023-07-11 10:20:30", updatedCompositionDto.getCreated());
        verify(compositionService, times(1)).updateComposition(composition);
        verify(compositionMapper, times(1)).mapToCompositionDto(composition);
        verify(compositionMapper, times(1)).mapToComposition(compositionDto);
    }
}
