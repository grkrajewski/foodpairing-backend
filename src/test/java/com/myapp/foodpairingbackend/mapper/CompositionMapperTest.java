package com.myapp.foodpairingbackend.mapper;

import com.myapp.foodpairingbackend.domain.dto.CompositionDto;
import com.myapp.foodpairingbackend.domain.entity.Composition;
import com.myapp.foodpairingbackend.domain.entity.Dish;
import com.myapp.foodpairingbackend.domain.entity.Drink;
import com.myapp.foodpairingbackend.service.DishService;
import com.myapp.foodpairingbackend.service.DrinkService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
class CompositionMapperTest {

    @Autowired
    private CompositionMapper compositionMapper;

    @MockBean
    private DishService dishService;

    @MockBean
    private DrinkService drinkService;

    //Given - data preparation
    Dish dish = Dish.builder().id(1L).externalSystemId(10L).name("test name").readyInMinutes(15)
            .servings(4).recipeUrl("https://test.com").compositionList(List.of())
            .build();
    Drink drink = Drink.builder().id(2L).externalSystemId("10").name("test name").alcoholic("test alcoholic")
            .glass("test glass").instructions("test instructions").drinkIngredientList(List.of())
            .build();

    @Test
    void testMapToComposition() throws Exception {
        //Given
        CompositionDto compositionDto = CompositionDto.builder().id(3L).dishId(dish.getId())
                .drinkId(drink.getId()).created(new Date()).commentList(List.of())
                .build();
        when(dishService.getDish(compositionDto.getDishId())).thenReturn(dish);
        when(drinkService.getDrink(compositionDto.getDrinkId())).thenReturn(drink);

        //When
        Composition composition = compositionMapper.mapToComposition(compositionDto);

        //Then
        assertEquals(3L, composition.getId());
        assertEquals(1L, composition.getDish().getId());
        assertEquals(2L, composition.getDrink().getId());
        assertNotNull(composition.getCreated());
        assertEquals(0, composition.getCommentList().size());
    }

    @Test
    void testMapToCompositionDto() {
        //Given
        Composition composition = Composition.builder().id(3L).dish(dish)
                .drink(drink).created(new Date()).commentList(List.of())
                .build();

        //When
        CompositionDto compositionDto = compositionMapper.mapToCompositionDto(composition);

        //Then
        assertEquals(3L, compositionDto.getId());
        assertEquals(1L, compositionDto.getDishId());
        assertEquals(2L, compositionDto.getDrinkId());
        assertNotNull(compositionDto.getCreated());
        assertEquals(0, compositionDto.getCommentList().size());
    }

    @Test
    void testMapToCompositionList() throws Exception {
        //Given
        CompositionDto compositionDto = CompositionDto.builder().id(3L).dishId(dish.getId())
                .drinkId(drink.getId()).created(new Date()).commentList(List.of())
                .build();
        List<CompositionDto> compositionDtoList = List.of(compositionDto);
        when(dishService.getDish(compositionDto.getDishId())).thenReturn(dish);
        when(drinkService.getDrink(compositionDto.getDrinkId())).thenReturn(drink);

        //When
        List<Composition> compositionList = compositionMapper.mapToCompositionList(compositionDtoList);

        //Then
        assertEquals(1, compositionList.size());
        assertEquals(3L, compositionList.get(0).getId());
        assertEquals(1L, compositionList.get(0).getDish().getId());
        assertEquals(2L, compositionList.get(0).getDrink().getId());
        assertNotNull(compositionList.get(0).getCreated());
        assertEquals(0, compositionList.get(0).getCommentList().size());
    }

    @Test
    void testMapToCompositionDtoList() {
        //Given
        Composition composition = Composition.builder().id(3L).dish(dish)
                .drink(drink).created(new Date()).commentList(List.of())
                .build();
        List<Composition> compositionList = List.of(composition);

        //When
        List<CompositionDto> compositionDtoList = compositionMapper.mapToCompositionDtoList(compositionList);

        //Then
        assertEquals(1, compositionDtoList.size());
        assertEquals(3L, compositionDtoList.get(0).getId());
        assertEquals(1L, compositionDtoList.get(0).getDishId());
        assertEquals(2L, compositionDtoList.get(0).getDrinkId());
        assertNotNull(compositionDtoList.get(0).getCreated());
        assertEquals(0, compositionDtoList.get(0).getCommentList().size());
    }
}