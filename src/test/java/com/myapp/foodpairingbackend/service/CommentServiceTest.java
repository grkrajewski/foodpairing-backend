package com.myapp.foodpairingbackend.service;

import com.myapp.foodpairingbackend.domain.entity.Comment;
import com.myapp.foodpairingbackend.domain.entity.Composition;
import com.myapp.foodpairingbackend.domain.entity.Dish;
import com.myapp.foodpairingbackend.domain.entity.Drink;
import com.myapp.foodpairingbackend.exception.ComponentExistsException;
import com.myapp.foodpairingbackend.exception.ComponentNotFoundException;
import com.myapp.foodpairingbackend.exception.IdException;
import com.myapp.foodpairingbackend.repository.CommentRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@Transactional
@SpringBootTest
class CommentServiceTest {

    @Autowired
    private CompositionService compositionService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private CommentRepository commentRepository;

    @InjectMocks
    private CommentService commentServiceMock;

    @Mock
    private CommentRepository commentRepositoryMock;

    //Given - data preparation
    Dish dish = Dish.builder()
            .id(null).externalSystemId(1L).name("test name dish").readyInMinutes(10).servings(4)
            .recipeUrl("https://test.com").compositionList(List.of())
            .build();

    Drink drink = Drink.builder()
            .id(null).externalSystemId("2").name("test name drink").alcoholic("test alcoholic")
            .glass("test glass").instructions("test instructions").drinkIngredientList(List.of())
            .build();

    Composition composition = Composition.builder()
            .id(null).dish(dish).drink(drink).created(LocalDateTime.now()).commentList(List.of())
            .build();

    Comment comment = Comment.builder().id(null).description("test description").created(LocalDateTime.now())
            .composition(composition).reactionList(List.of())
            .build();

    @Test
    void testGetComments() {
        //Given
        when(commentRepositoryMock.findAll()).thenReturn(List.of(comment));

        //When
        List<Comment> comments = commentServiceMock.getComments();

        //Then
        assertEquals(1, comments.size());
        verify(commentRepositoryMock, times(1)).findAll();
    }

    @Test
    void testGetComments_ShouldFetchEmptyList() {
        //When
        List<Comment> comments = commentServiceMock.getComments();

        //Then
        assertEquals(0, comments.size());
        verify(commentRepositoryMock, times(1)).findAll();
    }

    @Test
    void testGetCommentsForComposition() throws ComponentExistsException, ComponentNotFoundException, IdException {
        //Given
        compositionService.saveComposition(composition);
        commentService.saveComment(comment);
        Long compositionId = composition.getId();
        Long commentId = comment.getId();

        //When
        List<Comment> savedCommentList = commentService.getCommentsForComposition(compositionId);

        //Then
        assertTrue(commentRepository.existsById(commentId));
        assertEquals(1, savedCommentList.size());
        assertEquals("test description", savedCommentList.get(0).getDescription());
        assertNotNull(savedCommentList.get(0).getCreated());
        assertNotNull(savedCommentList.get(0).getComposition());
        assertEquals(0, savedCommentList.get(0).getReactionList().size());
    }

    @Test
    void testGetCommentsForComposition_ShouldGetEmptyList() throws ComponentExistsException, ComponentNotFoundException, IdException {
        //Given
        compositionService.saveComposition(composition);
        Long compositionId = composition.getId();

        //When
        List<Comment> commentList = commentService.getCommentsForComposition(compositionId);

        //Then
        assertEquals(0, commentList.size());
    }

    @Test
    void testGetCommentsForComposition_ShouldThrowComponentNotFoundException() {
        //When & Then
        assertThrows(ComponentNotFoundException.class, () -> commentService.getCommentsForComposition(1L));
    }

    @Test
    void testGetComment() throws ComponentExistsException, ComponentNotFoundException, IdException {
        //Given
        compositionService.saveComposition(composition);
        commentService.saveComment(comment);
        Long commentId = comment.getId();

        //When
        Comment savedComment = commentService.getComment(commentId);

        //Then
        assertEquals("test description", savedComment.getDescription());
        assertNotNull(savedComment.getCreated());
        assertNotNull(savedComment.getComposition());
        assertEquals(0, savedComment.getReactionList().size());
    }

    @Test
    void testDeleteComment() throws ComponentExistsException, ComponentNotFoundException, IdException {
        //Given
        compositionService.saveComposition(composition);
        commentService.saveComment(comment);
        Long commentId = comment.getId();

        //When
        commentService.deleteComment(commentId);

        //Then
        assertFalse(commentRepository.existsById(commentId));
    }

    @Test
    void testDeleteComment_ShouldThrowComponentNotFoundException() {
        //When & Then
        assertThrows(ComponentNotFoundException.class, () -> commentService.deleteComment(1L));
    }

    @Test
    void testSaveComment() throws ComponentExistsException, IdException {
        //Given
        compositionService.saveComposition(composition);

        //When
        commentService.saveComment(comment);
        Long commentId = comment.getId();

        //Then
        assertTrue(commentRepository.existsById(commentId));
    }

    @Test
    void testSaveComment_ShouldThrowIdException() {
        //Given
        Comment commentWithId = Comment.builder().id(1L).description("test description").created(LocalDateTime.now())
                .composition(composition).reactionList(List.of())
                .build();

        //When & Then
        assertThrows(IdException.class, () -> commentService.saveComment(commentWithId));
    }

    @Test
    void testUpdateComment() throws ComponentExistsException, IdException, ComponentNotFoundException {
        //Given
        compositionService.saveComposition(composition);
        commentService.saveComment(comment);
        Long commentId = comment.getId();
        Comment descriptionUpdatedComment = Comment.builder().id(commentId).description("test updated description").created(LocalDateTime.now())
                .composition(composition).reactionList(List.of())
                .build();

        //When
        Comment updatedComment = commentService.updateComment(descriptionUpdatedComment);

        //Then
        assertEquals("test updated description", updatedComment.getDescription());
    }

    @Test
    void testUpdateComment_ShouldThrowIdException() {
        //When & Then
        assertThrows(IdException.class, () -> commentService.updateComment(comment));
    }
}