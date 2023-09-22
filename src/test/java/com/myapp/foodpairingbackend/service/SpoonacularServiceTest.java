package com.myapp.foodpairingbackend.service;

import com.myapp.foodpairingbackend.client.SpoonacularClient;
import com.myapp.foodpairingbackend.domain.dto.SpoonacularDishDto;
import com.myapp.foodpairingbackend.domain.dto.SpoonacularResultDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@SpringBootTest
public class SpoonacularServiceTest {

    @MockBean
    private SpoonacularClient spoonacularClient;

    @Autowired
    private SpoonacularService spoonacularService;

    @Test
    void testShouldGetSpoonacularDishes() {
        //Given
        SpoonacularDishDto spoonacularDishDto = SpoonacularDishDto.builder()
                .externalSystemId(1L).name("test name 1").readyInMinutes(20)
                .servings(2).recipeUrl("https://test.com")
                .build();
        List<SpoonacularDishDto> spoonacularDishDtoList = List.of(spoonacularDishDto);
        SpoonacularResultDto resultDto = new SpoonacularResultDto(spoonacularDishDtoList);
        when(spoonacularClient.getDishesFromExternalApiDb("test name")).thenReturn(resultDto);

        //When
        List<SpoonacularDishDto> resultList = spoonacularService.getSpoonacularDishes("test name");

        //Then
        assertEquals(resultDto.getResultList(), resultList);
        assertEquals(1, resultList.size());
        verify(spoonacularClient, times(1)).getDishesFromExternalApiDb("test name");
    }
}
