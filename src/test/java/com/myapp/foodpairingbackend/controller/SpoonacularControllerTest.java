package com.myapp.foodpairingbackend.controller;

import com.myapp.foodpairingbackend.domain.dto.SpoonacularDishDto;
import com.myapp.foodpairingbackend.service.SpoonacularService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

import static org.mockito.Mockito.when;

@SpringJUnitWebConfig
@WebMvcTest(SpoonacularController.class)
class SpoonacularControllerTest {

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SpoonacularService spoonacularService;

    @Test
    void testShouldGetEmptySpoonacularDishes() throws Exception {
        //Given
        when(spoonacularService.getSpoonacularDishes("test")).thenReturn(List.of());

        //When & Then
        mockMvc
                .perform(MockMvcRequestBuilders
                        .get("/foodpairing/v1/spoonacular/dishes/test")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(MockMvcResultMatchers.status().is(200))
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(0)));
    }

    @Test
    void testShouldGetSpoonacularDishes() throws Exception {
        //Given
        SpoonacularDishDto spoonacularDishDto = SpoonacularDishDto.builder().externalSystemId(1L).name("test name")
                .readyInMinutes(15).servings(4).recipeUrl("https://test.com")
                .build();
        List<SpoonacularDishDto> spoonacularDishDtoList = List.of(spoonacularDishDto);
        when(spoonacularService.getSpoonacularDishes("test")).thenReturn(spoonacularDishDtoList);

        //When & Then
        mockMvc
                .perform(MockMvcRequestBuilders
                        .get("/foodpairing/v1/spoonacular/dishes/test")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id", Matchers.is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].title", Matchers.is("test name")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].readyInMinutes", Matchers.is(15)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].servings", Matchers.is(4)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].sourceUrl", Matchers.is("https://test.com")));
    }
}