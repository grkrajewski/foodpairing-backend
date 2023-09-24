package com.myapp.foodpairingbackend.service;

import com.myapp.foodpairingbackend.client.TheCocktailDbClient;
import com.myapp.foodpairingbackend.domain.dto.TheCocktailDbDrinkDto;
import com.myapp.foodpairingbackend.domain.dto.TheCocktailDbResultDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@SpringBootTest
public class TheCocktailDbServiceTest {

    @MockBean
    TheCocktailDbClient theCocktailDbClient;

    @Autowired
    TheCocktailDbService theCocktailDbService;

    @Test
    void testShouldGetTheCocktailDrink() {
        //Given
        TheCocktailDbDrinkDto theCocktailDbDrinkDto = TheCocktailDbDrinkDto.builder()
                .externalSystemId("1").name("test name").alcoholic("test alcoholic")
                .glass("test glass").instructions("test instructions").ingredient1("test ingredient")
                .build();
        List<TheCocktailDbDrinkDto> theCocktailDbDrinkDtoList = List.of(theCocktailDbDrinkDto);
        TheCocktailDbResultDto resultDto = new TheCocktailDbResultDto(theCocktailDbDrinkDtoList);
        when(theCocktailDbClient.getRandomDrinkFromExternalApiDb()).thenReturn(resultDto);

        //When
        List<TheCocktailDbDrinkDto> resultList = theCocktailDbService.getTheCocktailDrink();

        //Then
        assertEquals(resultDto.getResultList(), resultList);
        assertEquals(1, resultList.size());
        verify(theCocktailDbClient, times(1)).getRandomDrinkFromExternalApiDb();
    }
}
