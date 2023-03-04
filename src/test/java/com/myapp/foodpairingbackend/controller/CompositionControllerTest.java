package com.myapp.foodpairingbackend.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.myapp.foodpairingbackend.domain.dto.CompositionDto;
import com.myapp.foodpairingbackend.facade.CompositionFacade;
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

import java.util.Date;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringJUnitWebConfig
@WebMvcTest(CompositionController.class)
class CompositionControllerTest {

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CompositionFacade compositionFacade;

    @Test
    void shouldGetEmptyCompositions() throws Exception {
        //Given
        when(compositionFacade.getCompositions()).thenReturn(List.of());

        //When & Then
        mockMvc
                .perform(MockMvcRequestBuilders
                        .get("/foodpairing/v1/compositions")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(MockMvcResultMatchers.status().is(200))
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(0)));
    }

    @Test
    void shouldGetCompositions() throws Exception {
        //Given
        CompositionDto compositionDto = CompositionDto.builder().id(3L).dishId(1L)
                .drinkId(2L).created(new Date()).commentList(List.of())
                .build();
        List<CompositionDto> compositionDtoList = List.of(compositionDto);
        when(compositionFacade.getCompositions()).thenReturn(compositionDtoList);

        //When & Then
        mockMvc
                .perform(MockMvcRequestBuilders
                        .get("/foodpairing/v1/compositions")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id", Matchers.is(3)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].dishId", Matchers.is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].drinkId", Matchers.is(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].created", Matchers.notNullValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].commentList", Matchers.hasSize(0)));
    }

    @Test
    void shouldGetComposition() throws Exception {
        //Given
        CompositionDto compositionDto = CompositionDto.builder().id(3L).dishId(1L)
                .drinkId(2L).created(new Date()).commentList(List.of())
                .build();
        when(compositionFacade.getComposition(compositionDto.getId())).thenReturn(compositionDto);

        //When & Then
        mockMvc
                .perform(MockMvcRequestBuilders
                        .get("/foodpairing/v1/compositions/3")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(MockMvcResultMatchers.status().is(200))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.is(3)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.dishId", Matchers.is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.drinkId", Matchers.is(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.created", Matchers.notNullValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.commentList", Matchers.hasSize(0)));
    }

    @Test
    void shouldDeleteComposition() throws Exception {
        //Given

        //When & Then
        mockMvc
                .perform(MockMvcRequestBuilders
                        .delete("/foodpairing/v1/compositions/1"))
                .andExpect(MockMvcResultMatchers.status().is(204));
    }

    @Test
    void shouldSaveComposition() throws Exception {
        //Given
        CompositionDto compositionDto = CompositionDto.builder().id(3L).dishId(1L)
                .drinkId(2L).created(new Date()).commentList(List.of())
                .build();
        when(compositionFacade.saveComposition(any(CompositionDto.class))).thenReturn(compositionDto);
        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ")
                .create();
        String jsonContent = gson.toJson(compositionDto);

        //When & Then
        mockMvc
                .perform(MockMvcRequestBuilders
                        .post("/foodpairing/v1/compositions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
                        .content(jsonContent))
                .andExpect(MockMvcResultMatchers.status().is(201))
                .andExpect(MockMvcResultMatchers.header().string("Location", "http://localhost/foodpairing/v1/compositions/3"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.is(3)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.dishId", Matchers.is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.drinkId", Matchers.is(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.created", Matchers.notNullValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.commentList", Matchers.hasSize(0)));
    }

    @Test
    void shouldUpdateComposition() throws Exception {
        //Given
        CompositionDto compositionDto = CompositionDto.builder().id(3L).dishId(1L)
                .drinkId(2L).created(new Date()).commentList(List.of())
                .build();
        when(compositionFacade.updateComposition(any(CompositionDto.class))).thenReturn(compositionDto);
        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ")
                .create();
        String jsonContent = gson.toJson(compositionDto);

        //When & Then
        mockMvc
                .perform(MockMvcRequestBuilders
                        .put("/foodpairing/v1/compositions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
                        .content(jsonContent))
                .andExpect(MockMvcResultMatchers.status().is(200))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.is(3)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.dishId", Matchers.is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.drinkId", Matchers.is(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.created", Matchers.notNullValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.commentList", Matchers.hasSize(0)));
    }
}