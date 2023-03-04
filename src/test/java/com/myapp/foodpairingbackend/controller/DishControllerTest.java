package com.myapp.foodpairingbackend.controller;

import com.google.gson.Gson;
import com.myapp.foodpairingbackend.domain.dto.DishDto;
import com.myapp.foodpairingbackend.domain.entity.Dish;
import com.myapp.foodpairingbackend.facade.DishFacade;
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
@WebMvcTest(DishController.class)
class DishControllerTest {

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DishFacade dishFacade;

    @Test
    void shouldGetEmptyDishes() throws Exception {
        //Given
        when(dishFacade.getDishes()).thenReturn(List.of());

        //When & Then
        mockMvc
                .perform(MockMvcRequestBuilders
                        .get("/foodpairing/v1/dishes")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(MockMvcResultMatchers.status().is(200))
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(0)));
    }

    @Test
    void shouldGetDishes() throws Exception {
        //Given
        DishDto dishDto = DishDto.builder().id(1L).externalSystemId(10L).name("test name").readyInMinutes(15)
                .servings(4).recipeUrl("https://test.com").compositionList(List.of())
                .build();
        List<DishDto> dishDtoList = List.of(dishDto);
        when(dishFacade.getDishes()).thenReturn(dishDtoList);

        //When & Then
        mockMvc
                .perform(MockMvcRequestBuilders
                        .get("/foodpairing/v1/dishes")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id", Matchers.is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].externalSystemId", Matchers.is(10)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name", Matchers.is("test name")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].readyInMinutes", Matchers.is(15)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].servings", Matchers.is(4)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].recipeUrl", Matchers.is("https://test.com")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].compositionList", Matchers.hasSize(0)));
    }

    @Test
    void shouldGetDish() throws Exception {
        //Given
        Dish dish = Dish.builder()
                .id(1L).externalSystemId(10L).name("test name").readyInMinutes(15)
                .servings(4).recipeUrl("https://test.com").compositionList(List.of())
                .build();
        DishDto dishDto = DishDto.builder().id(1L).externalSystemId(10L).name("test name dto").readyInMinutes(15)
                .servings(4).recipeUrl("https://testDto.com").compositionList(List.of())
                .build();
        when(dishFacade.getDish(dish.getId())).thenReturn(dishDto);

        //When & Then
        mockMvc
                .perform(MockMvcRequestBuilders
                        .get("/foodpairing/v1/dishes/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().is(200))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.externalSystemId", Matchers.is(10)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", Matchers.is("test name dto")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.readyInMinutes", Matchers.is(15)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.servings", Matchers.is(4)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.recipeUrl", Matchers.is("https://testDto.com")));
    }

    @Test
    void shouldDeleteDish() throws Exception {
        //Given

        //When & Then
        mockMvc
                .perform(MockMvcRequestBuilders
                        .delete("/foodpairing/v1/dishes/1"))
                .andExpect(MockMvcResultMatchers.status().is(204));
    }

    @Test
    void shouldSaveDish() throws Exception {
        DishDto dishDto = DishDto.builder().id(1L).externalSystemId(10L).name("test name dto").readyInMinutes(15)
                .servings(4).recipeUrl("https://testDto.com").compositionList(List.of())
                .build();
        when(dishFacade.saveDishInDb(any(DishDto.class))).thenReturn(dishDto);
        Gson gson = new Gson();
        String jsonContent = gson.toJson(dishDto);

        //When & Then
        mockMvc
                .perform(MockMvcRequestBuilders
                        .post("/foodpairing/v1/dishes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
                        .content(jsonContent))
                .andExpect(MockMvcResultMatchers.status().is(201))
                .andExpect(MockMvcResultMatchers.header().string("Location", "http://localhost/foodpairing/v1/dishes/1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.externalSystemId", Matchers.is(10)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", Matchers.is("test name dto")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.readyInMinutes", Matchers.is(15)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.servings", Matchers.is(4)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.recipeUrl", Matchers.is("https://testDto.com")));
    }

    @Test
    void shouldUpdateDish() throws Exception {
        DishDto dishDto = DishDto.builder().id(1L).externalSystemId(10L).name("test name dto").readyInMinutes(15)
                .servings(4).recipeUrl("https://testDto.com").compositionList(List.of())
                .build();
        when(dishFacade.updateDish(any(DishDto.class))).thenReturn(dishDto);
        Gson gson = new Gson();
        String jsonContent = gson.toJson(dishDto);

        //When & Then
        mockMvc
                .perform(MockMvcRequestBuilders
                        .put("/foodpairing/v1/dishes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
                        .content(jsonContent))
                .andExpect(MockMvcResultMatchers.status().is(200))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.externalSystemId", Matchers.is(10)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", Matchers.is("test name dto")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.readyInMinutes", Matchers.is(15)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.servings", Matchers.is(4)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.recipeUrl", Matchers.is("https://testDto.com")));
    }
}