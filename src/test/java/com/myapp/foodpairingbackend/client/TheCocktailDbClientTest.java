package com.myapp.foodpairingbackend.client;

import com.myapp.foodpairingbackend.domain.dto.TheCocktailDbResultDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
public class TheCocktailDbClientTest {

    @MockBean
    private RestTemplate restTemplate;

    @Autowired
    private TheCocktailDbClient theCocktailDbClient;

    @Test
    void testShouldGetRandomDrinkFromExternalApiDb() {
        //Given
        TheCocktailDbResultDto mockApiResponse = new TheCocktailDbResultDto();
        when(restTemplate.getForObject(any(URI.class), any())).thenReturn(mockApiResponse);

        //When
        TheCocktailDbResultDto resultDto = theCocktailDbClient.getRandomDrinkFromExternalApiDb();

        //Then
        assertEquals(mockApiResponse, resultDto);
    }
}
