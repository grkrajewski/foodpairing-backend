package com.myapp.foodpairingbackend.controller;

import com.google.gson.Gson;
import com.myapp.foodpairingbackend.domain.dto.DrinkIngredientDto;
import com.myapp.foodpairingbackend.facade.DrinkIngredientFacade;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringJUnitWebConfig
@WebMvcTest(DrinkIngredientController.class)
class DrinkIngredientControllerTest {

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DrinkIngredientFacade drinkIngredientFacade;

    @Test
    void shouldGetEmptyDrinkIngredients() throws Exception {
        //Given
        when(drinkIngredientFacade.getDrinkIngredients()).thenReturn(List.of());

        //When & Then
        mockMvc
                .perform(MockMvcRequestBuilders
                        .get("/foodpairing/v1/drinkingredients")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(MockMvcResultMatchers.status().is(200))
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(0)));
    }

    @Test
    void shouldGetDrinkIngredients() throws Exception {
        //Given
        DrinkIngredientDto drinkIngredientDto = DrinkIngredientDto.builder().id(2L).
                name("test ingredient name").measure("test measure").drinkId(1L)
                .build();
        List<DrinkIngredientDto> drinkIngredientDtoList = List.of(drinkIngredientDto);
        when(drinkIngredientFacade.getDrinkIngredients()).thenReturn(drinkIngredientDtoList);

        //When & Then
        mockMvc
                .perform(MockMvcRequestBuilders
                        .get("/foodpairing/v1/drinkingredients")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id", Matchers.is(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name", Matchers.is("test ingredient name")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].measure", Matchers.is("test measure")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].drinkId", Matchers.is(1)));
    }

    @Test
    void shouldGetDrinkIngredientsForDrink() throws Exception {
        //Given
        DrinkIngredientDto drinkIngredientDto = DrinkIngredientDto.builder().id(2L).
                name("test ingredient name").measure("test measure").drinkId(1L)
                .build();
        List<DrinkIngredientDto> drinkIngredientDtoList = List.of(drinkIngredientDto);
        when(drinkIngredientFacade.getDrinkIngredientsForDrink(1L)).thenReturn(drinkIngredientDtoList);

        //When & Then
        mockMvc
                .perform(MockMvcRequestBuilders
                        .get("/foodpairing/v1/drinkingredients/for-drink/1")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id", Matchers.is(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name", Matchers.is("test ingredient name")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].measure", Matchers.is("test measure")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].drinkId", Matchers.is(1)));
    }

    @Test
    void shouldDeleteDrinkIngredient() throws Exception {
        //Given

        //When & Then
        mockMvc
                .perform(MockMvcRequestBuilders
                        .delete("/foodpairing/v1/drinkingredients/1"))
                .andExpect(MockMvcResultMatchers.status().is(204));
    }

    @Test
    void shouldSaveDrinkIngredient() throws Exception {
        //Given
        DrinkIngredientDto drinkIngredientDto = DrinkIngredientDto.builder().id(2L).
                name("test ingredient name").measure("test measure").drinkId(1L)
                .build();
        when(drinkIngredientFacade.saveDrinkIngredient(any(DrinkIngredientDto.class))).thenReturn(drinkIngredientDto);
        Gson gson = new Gson();
        String jsonContent = gson.toJson(drinkIngredientDto);

        //When & Then
        mockMvc
                .perform(MockMvcRequestBuilders
                        .post("/foodpairing/v1/drinkingredients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
                        .content(jsonContent))
                .andExpect(MockMvcResultMatchers.status().is(201))
                .andExpect(MockMvcResultMatchers.header().string("Location", "http://localhost/foodpairing/v1/drinkingredients/2"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.is(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", Matchers.is("test ingredient name")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.measure", Matchers.is("test measure")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.drinkId", Matchers.is(1)));
    }

    @Test
    void shouldUpdateDrinkIngredient() throws Exception {
        //Given
        DrinkIngredientDto drinkIngredientDto = DrinkIngredientDto.builder().id(2L).
                name("test ingredient name").measure("test measure").drinkId(1L)
                .build();
        when(drinkIngredientFacade.updateDrinkIngredient(any(DrinkIngredientDto.class))).thenReturn(drinkIngredientDto);
        Gson gson = new Gson();
        String jsonContent = gson.toJson(drinkIngredientDto);

        //When & Then
        mockMvc
                .perform(MockMvcRequestBuilders
                        .put("/foodpairing/v1/drinkingredients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
                        .content(jsonContent))
                .andExpect(MockMvcResultMatchers.status().is(200))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.is(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", Matchers.is("test ingredient name")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.measure", Matchers.is("test measure")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.drinkId", Matchers.is(1)));
    }
}