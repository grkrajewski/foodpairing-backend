package com.myapp.foodpairingbackend.controller;

import com.google.gson.Gson;
import com.myapp.foodpairingbackend.domain.dto.DrinkDto;
import com.myapp.foodpairingbackend.domain.dto.DrinkIngredientDto;
import com.myapp.foodpairingbackend.domain.entity.Drink;
import com.myapp.foodpairingbackend.domain.entity.DrinkIngredient;
import com.myapp.foodpairingbackend.facade.DrinkFacade;
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

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringJUnitWebConfig
@WebMvcTest(DrinkController.class)
class DrinkControllerTest {

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DrinkFacade drinkFacade;

    @Test
    void shouldGetEmptyDrinks() throws Exception {
        //Given
        when(drinkFacade.getDrinks()).thenReturn(List.of());

        //When & Then
        mockMvc
                .perform(MockMvcRequestBuilders
                        .get("/foodpairing/v1/drinks")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(MockMvcResultMatchers.status().is(200))
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(0)));
    }

    @Test
    void shouldGetDrinks() throws Exception {
        //Given
        List<DrinkIngredientDto> drinkIngredientDtoList = new ArrayList<>();
        DrinkIngredientDto drinkIngredientDto = DrinkIngredientDto.builder().id(2L).
                name("test ingredient name").measure("test measure").drinkId(1L)
                .build();
        drinkIngredientDtoList.add(drinkIngredientDto);
        DrinkDto drinkDto = DrinkDto.builder().id(1L).externalSystemId("10").name("test drink name").alcoholic("test alcoholic")
                .glass("test glass").instructions("test instructions").drinkIngredientList(drinkIngredientDtoList)
                .build();
        List<DrinkDto> drinkDtoList = List.of(drinkDto);
        when(drinkFacade.getDrinks()).thenReturn(drinkDtoList);

        //When & Then
        mockMvc
                .perform(MockMvcRequestBuilders
                        .get("/foodpairing/v1/drinks")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id", Matchers.is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].externalSystemId", Matchers.is("10")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name", Matchers.is("test drink name")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].alcoholic", Matchers.is("test alcoholic")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].glass", Matchers.is("test glass")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].instructions", Matchers.is("test instructions")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].drinkIngredientList", Matchers.hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].drinkIngredientList[0].id", Matchers.is(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].drinkIngredientList[0].name", Matchers.is("test ingredient name")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].drinkIngredientList[0].measure", Matchers.is("test measure")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].drinkIngredientList[0].drinkId", Matchers.is(1)));
    }

    @Test
    void shouldGetDrink() throws Exception {
        //Given
        List<DrinkIngredient> drinkIngredientList = new ArrayList<>();
        Drink drink = Drink.builder().id(1L).externalSystemId("10").name("test drink name").alcoholic("test alcoholic")
                .glass("test glass").instructions("test instructions").drinkIngredientList(drinkIngredientList)
                .build();
        DrinkIngredient drinkIngredient = DrinkIngredient.builder().id(2L).
                name("test ingredient name").measure("test measure").drink(drink)
                .build();
        drinkIngredientList.add(drinkIngredient);

        List<DrinkIngredientDto> drinkIngredientDtoList = new ArrayList<>();
        DrinkDto drinkDto = DrinkDto.builder().id(1L).externalSystemId("10").name("test dto drink name").alcoholic("test dto alcoholic")
                .glass("test dto glass").instructions("test dto instructions").drinkIngredientList(drinkIngredientDtoList)
                .build();
        DrinkIngredientDto drinkIngredientDto = DrinkIngredientDto.builder().id(2L).
                name("test dto ingredient name").measure("test dto measure").drinkId(drinkDto.getId())
                .build();
        drinkIngredientDtoList.add(drinkIngredientDto);
        when(drinkFacade.getDrink(drinkDto.getId())).thenReturn(drinkDto);

        //When & Then
        mockMvc
                .perform(MockMvcRequestBuilders
                        .get("/foodpairing/v1/drinks/1")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(MockMvcResultMatchers.status().is(200))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.externalSystemId", Matchers.is("10")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", Matchers.is("test dto drink name")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.alcoholic", Matchers.is("test dto alcoholic")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.glass", Matchers.is("test dto glass")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.instructions", Matchers.is("test dto instructions")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.drinkIngredientList", Matchers.hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.drinkIngredientList[0].id", Matchers.is(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.drinkIngredientList[0].name", Matchers.is("test dto ingredient name")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.drinkIngredientList[0].measure", Matchers.is("test dto measure")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.drinkIngredientList[0].drinkId", Matchers.is(1)));
    }

    @Test
    void shouldDeleteDrink() throws Exception {
        //Given

        //When & Then
        mockMvc
                .perform(MockMvcRequestBuilders
                        .delete("/foodpairing/v1/drinks/1"))
                .andExpect(MockMvcResultMatchers.status().is(200));
    }

    @Test
    void shouldSaveDrink() throws Exception {
        //Given
        List<DrinkIngredientDto> drinkIngredientDtoList = new ArrayList<>();
        DrinkDto drinkDto = DrinkDto.builder().id(1L).externalSystemId("10").name("test dto drink name").alcoholic("test dto alcoholic")
                .glass("test dto glass").instructions("test dto instructions").drinkIngredientList(drinkIngredientDtoList)
                .build();
        DrinkIngredientDto drinkIngredientDto = DrinkIngredientDto.builder().id(2L).
                name("test dto ingredient name").measure("test dto measure").drinkId(drinkDto.getId())
                .build();
        drinkIngredientDtoList.add(drinkIngredientDto);
        when(drinkFacade.saveDrinkInDb(any(DrinkDto.class))).thenReturn(drinkDto);
        Gson gson = new Gson();
        String jsonContent = gson.toJson(drinkDto);

        //When & Then
        mockMvc
                .perform(MockMvcRequestBuilders
                        .post("/foodpairing/v1/drinks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
                        .content(jsonContent))
                .andExpect(MockMvcResultMatchers.status().is(200))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.externalSystemId", Matchers.is("10")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", Matchers.is("test dto drink name")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.alcoholic", Matchers.is("test dto alcoholic")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.glass", Matchers.is("test dto glass")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.instructions", Matchers.is("test dto instructions")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.drinkIngredientList", Matchers.hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.drinkIngredientList[0].id", Matchers.is(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.drinkIngredientList[0].name", Matchers.is("test dto ingredient name")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.drinkIngredientList[0].measure", Matchers.is("test dto measure")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.drinkIngredientList[0].drinkId", Matchers.is(1)));
    }

    @Test
    void shouldUpdateDrink() throws Exception {
        //Given
        List<DrinkIngredientDto> drinkIngredientDtoList = new ArrayList<>();
        DrinkDto drinkDto = DrinkDto.builder().id(1L).externalSystemId("10").name("test dto drink name").alcoholic("test dto alcoholic")
                .glass("test dto glass").instructions("test dto instructions").drinkIngredientList(drinkIngredientDtoList)
                .build();
        DrinkIngredientDto drinkIngredientDto = DrinkIngredientDto.builder().id(2L).
                name("test dto ingredient name").measure("test dto measure").drinkId(drinkDto.getId())
                .build();
        drinkIngredientDtoList.add(drinkIngredientDto);
        when(drinkFacade.updateDrink(any(DrinkDto.class))).thenReturn(drinkDto);
        Gson gson = new Gson();
        String jsonContent = gson.toJson(drinkDto);

        //When & Then
        mockMvc
                .perform(MockMvcRequestBuilders
                        .put("/foodpairing/v1/drinks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
                        .content(jsonContent))
                .andExpect(MockMvcResultMatchers.status().is(200))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.externalSystemId", Matchers.is("10")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", Matchers.is("test dto drink name")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.alcoholic", Matchers.is("test dto alcoholic")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.glass", Matchers.is("test dto glass")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.instructions", Matchers.is("test dto instructions")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.drinkIngredientList", Matchers.hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.drinkIngredientList[0].id", Matchers.is(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.drinkIngredientList[0].name", Matchers.is("test dto ingredient name")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.drinkIngredientList[0].measure", Matchers.is("test dto measure")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.drinkIngredientList[0].drinkId", Matchers.is(1)));
    }
}