package com.myapp.foodpairingbackend.facade;

import com.myapp.foodpairingbackend.domain.dto.CommentDto;
import com.myapp.foodpairingbackend.domain.entity.Comment;
import com.myapp.foodpairingbackend.domain.entity.Composition;
import com.myapp.foodpairingbackend.domain.entity.Dish;
import com.myapp.foodpairingbackend.domain.entity.Drink;
import com.myapp.foodpairingbackend.exception.ComponentNotFoundException;
import com.myapp.foodpairingbackend.exception.IdException;
import com.myapp.foodpairingbackend.mapper.CommentMapper;
import com.myapp.foodpairingbackend.service.CommentService;
import com.myapp.foodpairingbackend.service.CompositionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class CommentFacadeTest {

    @Autowired
    private CommentFacade commentFacade;

    @MockBean
    private CommentService commentService;

    @MockBean
    private CompositionService compositionService;

    @SpyBean
    private CommentMapper commentMapper;

    private Composition composition;
    private Comment comment;
    private CommentDto commentDto;

    @BeforeEach
    void setup() {
        //Given
        Dish dish = Dish.builder()
                .id(1L).externalSystemId(10L).name("test name dish").readyInMinutes(10).servings(4)
                .recipeUrl("https://test.com").compositionList(List.of())
                .build();

        Drink drink = Drink.builder()
                .id(2L).externalSystemId("20").name("test name drink").alcoholic("test alcoholic")
                .glass("test glass").instructions("test instructions").drinkIngredientList(List.of())
                .build();

        composition = Composition.builder()
                .id(3L).dish(dish).drink(drink).created(LocalDateTime.now()).commentList(List.of())
                .build();

        comment = Comment.builder().
                id(4L).description("test description").created(LocalDateTime.of(2023, 7, 10, 13, 12, 33))
                .composition(composition).reactionList(List.of())
                .build();

        commentDto = CommentDto.builder().
                id(4L).description("test description").created("2023-07-10 13:12:33")
                .compositionId(composition.getId()).reactionList(List.of())
                .build();
    }

    @Test
    void testGetComments() {
        //Given
        when(commentService.getComments()).thenReturn(List.of(comment));

        //When
        List<CommentDto> comments = commentFacade.getComments();

        //Then
        assertEquals(1, comments.size());
        verify(commentService, times(1)).getComments();
        verify(commentMapper, times(1)).mapToCommentDtoList(List.of(comment));
    }

    @Test
    void testGetCommentsForComposition() throws ComponentNotFoundException {
        //Given
        when(commentService.getCommentsForComposition(composition.getId())).thenReturn(List.of(comment));

        //When
        List<CommentDto> comments = commentFacade.getCommentsForComposition(composition.getId());

        //Then
        assertEquals(1, comments.size());
        verify(commentService, times(1)).getCommentsForComposition(3L);
        verify(commentMapper, times(1)).mapToCommentDtoList(List.of(comment));
    }

    @Test
    void testGetComposition() throws ComponentNotFoundException {
        //Given
        when(commentService.getComment(comment.getId())).thenReturn(comment);

        //When
        CommentDto fetchedCommentDto = commentFacade.getComment(comment.getId());

        //Then
        assertEquals(4L, fetchedCommentDto.getId());
        assertEquals("test description", fetchedCommentDto.getDescription());
        assertEquals("2023-07-10 13:12:33", fetchedCommentDto.getCreated());
        assertEquals(3L, fetchedCommentDto.getCompositionId());
        assertTrue(fetchedCommentDto.getReactionList().isEmpty());
        verify(commentService, times(1)).getComment(4L);
        verify(commentMapper, times(1)).mapToCommentDto(comment);
    }

    @Test
    void testDeleteComposition() throws ComponentNotFoundException {
        //Given
        doNothing().when(commentService).deleteComment(comment.getId());

        //When
        commentFacade.deleteComment(comment.getId());

        //Then
        verify(commentService, times(1)).deleteComment(4L);
    }

    @Test
    void testSaveComment() throws IdException, ComponentNotFoundException {
        //Given
        when(commentService.saveComment(any(Comment.class))).thenReturn(comment);

        //When
        CommentDto savedCommentDto = commentFacade.saveComment(commentDto);

        //Then
        assertEquals(4L, savedCommentDto.getId());
        assertEquals("test description", savedCommentDto.getDescription());
        assertEquals("2023-07-10 13:12:33", savedCommentDto.getCreated());
        assertEquals(3L, savedCommentDto.getCompositionId());
        assertTrue(savedCommentDto.getReactionList().isEmpty());
        verify(commentService, times(1)).saveComment(any(Comment.class));
        verify(commentMapper, times(1)).mapToCommentDto(comment);
        verify(commentMapper, times(1)).mapToComment(commentDto);
    }

    @Test
    void testUpdateComment() throws ComponentNotFoundException, IdException {
        //Given
        when(commentService.updateComment(any(Comment.class))).thenAnswer(answer -> {
            ReflectionTestUtils.setField(comment, "created",
                    LocalDateTime.of(2023, 7, 11, 10, 20, 30));
            return comment;
        });

        //When
        CommentDto updatedCommentDto = commentFacade.updateComment(commentDto);

        //Then
        assertEquals(4L, updatedCommentDto.getId());
        assertEquals("test description", updatedCommentDto.getDescription());
        assertEquals("2023-07-11 10:20:30", updatedCommentDto.getCreated());
        assertEquals(3L, updatedCommentDto.getCompositionId());
        assertTrue(updatedCommentDto.getReactionList().isEmpty());
        verify(commentService, times(1)).updateComment(any(Comment.class));
        verify(commentMapper, times(1)).mapToCommentDto(comment);
        verify(commentMapper, times(1)).mapToComment(commentDto);
    }
}
