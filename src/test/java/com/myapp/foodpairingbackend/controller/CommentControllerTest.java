package com.myapp.foodpairingbackend.controller;

import com.google.gson.Gson;
import com.myapp.foodpairingbackend.domain.dto.CommentDto;
import com.myapp.foodpairingbackend.facade.CommentFacade;
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
import java.util.Date;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringJUnitWebConfig
@WebMvcTest(CommentController.class)
class CommentControllerTest {

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CommentFacade commentFacade;

    @Test
    void shouldGetEmptyCompositions() throws Exception {
        //Given
        when(commentFacade.getComments()).thenReturn(List.of());

        //When & Then
        mockMvc
                .perform(MockMvcRequestBuilders
                        .get("/foodpairing/v1/comments")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(MockMvcResultMatchers.status().is(200))
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(0)));
    }

    @Test
    void shouldGetComments() throws Exception {
        //Given
        List<CommentDto> commentDtoList = new ArrayList<>();
        CommentDto commentDto = CommentDto.builder().id(1L).description("test description")
                .created(new Date()).compositionId(2L).reactionList(List.of())
                .build();
        commentDtoList.add(commentDto);
        when(commentFacade.getComments()).thenReturn(commentDtoList);

        //When & Then
        mockMvc
                .perform(MockMvcRequestBuilders
                        .get("/foodpairing/v1/comments")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id", Matchers.is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].description", Matchers.is("test description")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].compositionId", Matchers.is(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].reactionList", Matchers.hasSize(0)));
    }

    @Test
    void shouldGetCommentsForComposition() throws Exception {
        //Given
        List<CommentDto> commentDtoList = new ArrayList<>();
        CommentDto commentDto = CommentDto.builder().id(1L).description("test description")
                .created(new Date()).compositionId(2L).reactionList(List.of())
                .build();
        commentDtoList.add(commentDto);
        when(commentFacade.getCommentsForComposition(2L)).thenReturn(commentDtoList);

        //When & Then
        mockMvc
                .perform(MockMvcRequestBuilders
                        .get("/foodpairing/v1/comments/2")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id", Matchers.is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].description", Matchers.is("test description")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].compositionId", Matchers.is(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].reactionList", Matchers.hasSize(0)));
    }

    @Test
    void shouldDeleteComments() throws Exception {
        //Given

        //When & Then
        mockMvc
                .perform(MockMvcRequestBuilders
                        .delete("/foodpairing/v1/comments/1"))
                .andExpect(MockMvcResultMatchers.status().is(200));
    }

    @Test
    void shouldSaveComments() throws Exception {
        //Given
        CommentDto commentDto = CommentDto.builder().id(1L).description("test description")
                .created(null).compositionId(2L).reactionList(List.of())
                .build();
        when(commentFacade.saveComment(any(CommentDto.class))).thenReturn(commentDto);
        Gson gson = new Gson();
        String jsonContent = gson.toJson(commentDto);

        //When & Then
        mockMvc
                .perform(MockMvcRequestBuilders
                        .post("/foodpairing/v1/comments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
                        .content(jsonContent))
                .andExpect(MockMvcResultMatchers.status().is(200))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description", Matchers.is("test description")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.compositionId", Matchers.is(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.reactionList", Matchers.hasSize(0)));
    }

    @Test
    void shouldUpdateComments() throws Exception {
        //Given
        CommentDto commentDto = CommentDto.builder().id(1L).description("test description")
                .created(null).compositionId(2L).reactionList(List.of())
                .build();
        when(commentFacade.updateComment(any(CommentDto.class))).thenReturn(commentDto);
        Gson gson = new Gson();
        String jsonContent = gson.toJson(commentDto);

        //When & Then
        mockMvc
                .perform(MockMvcRequestBuilders
                        .put("/foodpairing/v1/comments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
                        .content(jsonContent))
                .andExpect(MockMvcResultMatchers.status().is(200))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description", Matchers.is("test description")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.compositionId", Matchers.is(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.reactionList", Matchers.hasSize(0)));
    }
}