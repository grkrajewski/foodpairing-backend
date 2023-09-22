package com.myapp.foodpairingbackend.client;

import com.myapp.foodpairingbackend.domain.dto.SpoonacularResultDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@SpringBootTest
public class SpoonacularClientTest {

    @MockBean
    private RestTemplate restTemplate;

    @Autowired
    private SpoonacularClient spoonacularClient;

    @Test
    void testShouldGetDishesFromExternalApiDb() {
        //Given
        SpoonacularResultDto mockApiResponse = new SpoonacularResultDto();
        when(restTemplate.getForObject(any(URI.class), any())).thenReturn(mockApiResponse);

        //When
        SpoonacularResultDto resultDto = spoonacularClient.getDishesFromExternalApiDb("test name");

        //Then
        assertEquals(mockApiResponse, resultDto);
        verify(restTemplate, times(1)).getForObject(any(URI.class), any());
    }
}
