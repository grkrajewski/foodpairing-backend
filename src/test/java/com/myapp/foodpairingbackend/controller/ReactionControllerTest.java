package com.myapp.foodpairingbackend.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
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
    void testShouldGetEmptyReactions() throws Exception {
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
    void testShouldGetReactions() throws Exception {
        //Given
        ReactionDto reactionDto = ReactionDto.builder().id(1L).description("test description").created("2023-05-04 22:12:00").commentId(2L)
                .build();
        List<ReactionDto> reactionDtoList = List.of(reactionDto);
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
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].created", Matchers.is("2023-05-04 22:12:00")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].commentId", Matchers.is(2)));
    }

    @Test
    void testShouldGetReactionsForComment() throws Exception {
        //Given
        ReactionDto reactionDto = ReactionDto.builder().id(1L).description("test description").created("2023-05-04 22:12:00").commentId(2L)
                .build();
        List<ReactionDto> reactionDtoList = List.of(reactionDto);
        when(reactionFacade.getReactionsForComment(2L)).thenReturn(reactionDtoList);

        //When & Then
        mockMvc
                .perform(MockMvcRequestBuilders
                        .get("/foodpairing/v1/reactions/for-comment/2")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id", Matchers.is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].description", Matchers.is("test description")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].created", Matchers.is("2023-05-04 22:12:00")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].commentId", Matchers.is(2)));
    }

    @Test
    void testShouldGetReaction() throws Exception {
        //Given
        ReactionDto reactionDto = ReactionDto.builder().id(1L).description("test description").created("2023-05-04 22:12:00").commentId(2L)
                .build();
        when(reactionFacade.getReaction(reactionDto.getId())).thenReturn(reactionDto);

        //When & Then
        mockMvc
                .perform(MockMvcRequestBuilders
                        .get("/foodpairing/v1/reactions/1")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(MockMvcResultMatchers.status().is(200))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description", Matchers.is("test description")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.created", Matchers.is("2023-05-04 22:12:00")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.commentId", Matchers.is(2)));
    }

    @Test
    void testShouldDeleteReaction() throws Exception {
        //Given

        //When & Then
        mockMvc
                .perform(MockMvcRequestBuilders
                        .delete("/foodpairing/v1/reactions/1"))
                .andExpect(MockMvcResultMatchers.status().is(204));
    }

    @Test
    void testShouldSaveReaction() throws Exception {
        //Given
        ReactionDto reactionDto = ReactionDto.builder().id(1L).description("test description").created("2023-05-04 22:12:00").commentId(2L)
                .build();
        when(reactionFacade.saveReaction(any(ReactionDto.class))).thenReturn(reactionDto);
        Gson gson = new GsonBuilder()
                .create();
        String jsonContent = gson.toJson(reactionDto);

        //When & Then
        mockMvc
                .perform(MockMvcRequestBuilders
                        .post("/foodpairing/v1/reactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
                        .content(jsonContent))
                .andExpect(MockMvcResultMatchers.status().is(201))
                .andExpect(MockMvcResultMatchers.header().string("Location", "http://localhost/foodpairing/v1/reactions/1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description", Matchers.is("test description")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.created", Matchers.is("2023-05-04 22:12:00")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.commentId", Matchers.is(2)));
    }

    @Test
    void testShouldUpdateReaction() throws Exception {
        //Given
        ReactionDto reactionDto = ReactionDto.builder().id(1L).description("test description").created("2023-05-04 22:12:00").commentId(2L)
                .build();
        when(reactionFacade.updateReaction(any(ReactionDto.class))).thenReturn(reactionDto);
        Gson gson = new GsonBuilder()
                .create();
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
                .andExpect(MockMvcResultMatchers.jsonPath("$.created", Matchers.is("2023-05-04 22:12:00")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.commentId", Matchers.is(2)));
    }
}
