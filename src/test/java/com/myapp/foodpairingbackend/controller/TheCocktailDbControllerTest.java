package com.myapp.foodpairingbackend.controller;

import com.myapp.foodpairingbackend.domain.dto.TheCocktailDbDrinkDto;
import com.myapp.foodpairingbackend.service.TheCocktailDbService;
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
@WebMvcTest(TheCocktailDbController.class)
class TheCocktailDbControllerTest {

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TheCocktailDbService theCocktailDbService;

    @Test
    void getEmptyTheCocktailDbDrink() throws Exception {
        //Given
        when(theCocktailDbService.getTheCocktailDrink()).thenReturn(List.of());

        //When & Then
        mockMvc
                .perform(MockMvcRequestBuilders
                        .get("/foodpairing/v1/thecocktaildb/randomdrink")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(MockMvcResultMatchers.status().is(200))
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(0)));
    }

    @Test
    void getTheCocktailDbDrink() throws Exception {
        //Given
        TheCocktailDbDrinkDto theCocktailDbDrinkDto = TheCocktailDbDrinkDto.builder().externalSystemId("1").name("test name")
                .alcoholic("test alcoholic").glass("test glass").instructions("test instructions").ingredient1("test ingredient")
                .build();
        List<TheCocktailDbDrinkDto> theCocktailDbDrinkDtoList = List.of(theCocktailDbDrinkDto);
        when(theCocktailDbService.getTheCocktailDrink()).thenReturn(theCocktailDbDrinkDtoList);

        //When & Then
        mockMvc
                .perform(MockMvcRequestBuilders
                        .get("/foodpairing/v1/thecocktaildb/randomdrink")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].idDrink", Matchers.is("1")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].strDrink", Matchers.is("test name")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].strAlcoholic", Matchers.is("test alcoholic")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].strGlass", Matchers.is("test glass")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].strInstructions", Matchers.is("test instructions")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].strIngredient1", Matchers.is("test ingredient")));
    }
}