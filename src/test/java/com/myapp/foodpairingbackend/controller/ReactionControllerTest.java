package com.myapp.foodpairingbackend.controller;

import com.google.gson.Gson;
import com.myapp.foodpairingbackend.domain.dto.ReactionDto;
import com.myapp.foodpairingbackend.facade.ReactionFacade;
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
@WebMvcTest(ReactionController.class)
class ReactionControllerTest {

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ReactionFacade reactionFacade;

    @Test
    void shouldGetEmptyReactions() throws Exception {
        //Given
        when(reactionFacade.getReactions()).thenReturn(List.of());

        //When & Then
        mockMvc
                .perform(MockMvcRequestBuilders
                        .get("/foodpairing/v1/reactions")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(MockMvcResultMatchers.status().is(200))
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(0)));
    }

    @Test
    void shouldGetReactions() throws Exception {
        //Given
        List<ReactionDto> reactionDtoList = new ArrayList<>();
        ReactionDto reactionDto = ReactionDto.builder().id(1L).description("test description").created(null).commentId(2L)
                .build();
        reactionDtoList.add(reactionDto);
        when(reactionFacade.getReactions()).thenReturn(reactionDtoList);

        //When & Then
        mockMvc
                .perform(MockMvcRequestBuilders
                        .get("/foodpairing/v1/reactions")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id", Matchers.is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].description", Matchers.is("test description")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].commentId", Matchers.is(2)));
    }

    @Test
    void shouldGetReactionsForComment() throws Exception {
        //Given
        List<ReactionDto> reactionDtoList = new ArrayList<>();
        ReactionDto reactionDto = ReactionDto.builder().id(1L).description("test description").created(null).commentId(2L)
                .build();
        reactionDtoList.add(reactionDto);
        when(reactionFacade.getReactionsForComment(2L)).thenReturn(reactionDtoList);

        //When & Then
        mockMvc
                .perform(MockMvcRequestBuilders
                        .get("/foodpairing/v1/reactions/2")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id", Matchers.is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].description", Matchers.is("test description")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].commentId", Matchers.is(2)));
    }

    @Test
    void shouldDeleteReactions() throws Exception {
        //Given

        //When & Then
        mockMvc
                .perform(MockMvcRequestBuilders
                        .delete("/foodpairing/v1/reactions/1"))
                .andExpect(MockMvcResultMatchers.status().is(200));
    }

    @Test
    void shouldSaveReactions() throws Exception {
        //Given
        ReactionDto reactionDto = ReactionDto.builder().id(1L).description("test description").created(null).commentId(2L)
                .build();
        when(reactionFacade.saveReaction(any(ReactionDto.class))).thenReturn(reactionDto);
        Gson gson = new Gson();
        String jsonContent = gson.toJson(reactionDto);

        //When & Then
        mockMvc
                .perform(MockMvcRequestBuilders
                        .post("/foodpairing/v1/reactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
                        .content(jsonContent))
                .andExpect(MockMvcResultMatchers.status().is(200))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description", Matchers.is("test description")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.commentId", Matchers.is(2)));
    }

    @Test
    void shouldUpdateReactions() throws Exception {
        //Given
        ReactionDto reactionDto = ReactionDto.builder().id(1L).description("test description").created(null).commentId(2L)
                .build();
        when(reactionFacade.updateReaction(any(ReactionDto.class))).thenReturn(reactionDto);
        Gson gson = new Gson();
        String jsonContent = gson.toJson(reactionDto);

        //When & Then
        mockMvc
                .perform(MockMvcRequestBuilders
                        .put("/foodpairing/v1/reactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
                        .content(jsonContent))
                .andExpect(MockMvcResultMatchers.status().is(200))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description", Matchers.is("test description")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.commentId", Matchers.is(2)));
    }
}