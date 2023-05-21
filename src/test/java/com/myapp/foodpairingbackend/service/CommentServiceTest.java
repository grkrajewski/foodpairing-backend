package com.myapp.foodpairingbackend.service;

import com.myapp.foodpairingbackend.domain.entity.Comment;
import com.myapp.foodpairingbackend.domain.entity.Composition;
import com.myapp.foodpairingbackend.domain.entity.Dish;
import com.myapp.foodpairingbackend.domain.entity.Drink;
import com.myapp.foodpairingbackend.exception.ComponentNotFoundException;
import com.myapp.foodpairingbackend.exception.IdException;
import com.myapp.foodpairingbackend.repository.CommentRepository;
import com.myapp.foodpairingbackend.repository.CompositionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@SpringBootTest
class CommentServiceTest {

    @Autowired
    private CommentService commentService;

    @MockBean
    private CompositionRepository compositionRepository;

    @MockBean
    private CommentRepository commentRepository;

    private Composition composition;
    private Comment comment;

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
                id(4L).description("test description").created(LocalDateTime.now())
                .composition(composition).reactionList(List.of())
                .build();
    }

    @Test
    void testGetComments() {
        //Given
        when(commentRepository.findAll()).thenReturn(List.of(comment));

        //When
        List<Comment> comments = commentService.getComments();

        //Then
        assertEquals(1, comments.size());
        verify(commentRepository, times(1)).findAll();
    }

    @Test
    void testGetComments_ShouldFetchEmptyList() {
        //Given
        when(commentRepository.findAll()).thenReturn(List.of());

        //When
        List<Comment> comments = commentService.getComments();

        //Then
        assertEquals(0, comments.size());
        verify(commentRepository, times(1)).findAll();
    }

    @Test
    void testGetCommentsForComposition() throws ComponentNotFoundException {
        //Given
        when(compositionRepository.existsById(composition.getId())).thenReturn(true);
        when(commentRepository.findByCompositionId(composition.getId())).thenReturn(List.of(comment));

        //When
        List<Comment> savedCommentList = commentService.getCommentsForComposition(composition.getId());

        //Then
        assertEquals(1, savedCommentList.size());
        verify(commentRepository, times(1)).findByCompositionId(3L);
    }

    @Test
    void testGetCommentsForComposition_ShouldGetEmptyList() throws ComponentNotFoundException {
        //Given
        when(compositionRepository.existsById(composition.getId())).thenReturn(true);
        when(commentRepository.findByCompositionId(composition.getId())).thenReturn(List.of());

        //When
        List<Comment> commentList = commentService.getCommentsForComposition(composition.getId());

        //Then
        assertEquals(0, commentList.size());
        verify(commentRepository, times(1)).findByCompositionId(3L);
    }

    @Test
    void testGetCommentsForComposition_ShouldThrowComponentNotFoundException() {
        //Given
        when(compositionRepository.existsById(3L)).thenReturn(false);

        //When & Then
        assertThrows(ComponentNotFoundException.class, () -> commentService.getCommentsForComposition(3L));
    }

    @Test
    void testGetComment() throws ComponentNotFoundException {
        //Given
        when(commentRepository.findById(comment.getId())).thenReturn(Optional.ofNullable(comment));

        //When
        Comment savedComment = commentService.getComment(comment.getId());

        //Then
        assertEquals(4L, savedComment.getId());
        assertEquals("test description", savedComment.getDescription());
        assertNotNull(savedComment.getCreated());
        assertNotNull(savedComment.getComposition());
        assertEquals(0, savedComment.getReactionList().size());
        verify(commentRepository, times(1)).findById(4L);
    }

    @Test
    void testDeleteComment() throws ComponentNotFoundException {
        //Given
        doNothing().when(commentRepository).deleteById(comment.getId());

        //When
        commentService.deleteComment(comment.getId());

        //Then
        verify(commentRepository, times(1)).deleteById(4L);
    }

    @Test
    void testSaveComment() throws IdException {
        //Given
        ReflectionTestUtils.setField(comment, "id", null);
        when(commentRepository.save(any(Comment.class))).thenAnswer(answer -> {
            ReflectionTestUtils.setField(comment, "id", 4L);
            return comment;
        });

        //When
        Comment savedComment = commentService.saveComment(comment);

        //Then
        assertEquals(4L, savedComment.getId());
        assertEquals("test description", savedComment.getDescription());
        assertNotNull(savedComment.getCreated());
        assertNotNull(savedComment.getComposition());
        assertEquals(0, savedComment.getReactionList().size());
        verify(commentRepository, times(1)).save(comment);
    }

    @Test
    void testSaveComment_ShouldThrowIdException() {
        //When & Then
        assertThrows(IdException.class, () -> commentService.saveComment(comment));
    }

    @Test
    void testUpdateComment() throws IdException, ComponentNotFoundException {
        //Given
        when(commentRepository.findById(comment.getId())).thenReturn(Optional.ofNullable(comment));
        when(commentRepository.save(any(Comment.class))).thenAnswer(answer -> {
            ReflectionTestUtils.setField(comment, "description", "test updated description");
            return comment;
        });

        //When
        Comment updatedComment = commentService.updateComment(comment);

        //Then
        assertEquals("test updated description", updatedComment.getDescription());
    }

    @Test
    void testUpdateComment_ShouldThrowIdException() {
        //Given
        ReflectionTestUtils.setField(comment, "id", null);

        //When & Then
        assertThrows(IdException.class, () -> commentService.updateComment(comment));
    }
}
