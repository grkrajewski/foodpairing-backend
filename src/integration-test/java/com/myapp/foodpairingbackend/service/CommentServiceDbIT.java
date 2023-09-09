package com.myapp.foodpairingbackend.service;

import com.myapp.foodpairingbackend.domain.entity.Comment;
import com.myapp.foodpairingbackend.domain.entity.Composition;
import com.myapp.foodpairingbackend.domain.entity.Dish;
import com.myapp.foodpairingbackend.domain.entity.Drink;
import com.myapp.foodpairingbackend.exception.ComponentExistsException;
import com.myapp.foodpairingbackend.exception.ComponentNotFoundException;
import com.myapp.foodpairingbackend.exception.IdException;
import com.myapp.foodpairingbackend.repository.CommentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.util.ReflectionTestUtils;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@TestPropertySource("/application-test.properties")
public class CommentServiceDbIT {

    @Autowired
    private CompositionService compositionService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private CommentRepository commentRepository;

    private Composition composition;
    private Comment comment;

    @BeforeEach
    void setup() {
        //Given
        Dish dish = Dish.builder()
                .id(null).externalSystemId(1L).name("test name dish").readyInMinutes(10).servings(4)
                .recipeUrl("https://test.com").compositionList(List.of())
                .build();

        Drink drink = Drink.builder()
                .id(null).externalSystemId("2").name("test name drink").alcoholic("test alcoholic")
                .glass("test glass").instructions("test instructions").drinkIngredientList(List.of())
                .build();

        composition = Composition.builder()
                .id(null).dish(dish).drink(drink).created(LocalDateTime.now()).commentList(List.of())
                .build();

        comment = Comment.builder()
                .id(null).description("test comment description").created(LocalDateTime.now())
                .composition(composition).reactionList(List.of())
                .build();
    }

    @Test
    void testGetComments() throws ComponentExistsException, IdException {
        //Given
        compositionService.saveComposition(composition);
        commentService.saveComment(comment);

        //When
        List<Comment> comments = commentService.getComments();

        //Then
        assertEquals(1, comments.size());
    }

    @Test
    void testGetComments_ShouldFetchEmptyList() {
        //When
        List<Comment> comments = commentService.getComments();

        //Then
        assertTrue(comments.isEmpty());
    }

    @Test
    void testGetCommentsForComposition() throws ComponentExistsException, ComponentNotFoundException, IdException {
        //Given
        compositionService.saveComposition(composition);
        commentService.saveComment(comment);

        //When
        List<Comment> savedCommentList = commentService.getCommentsForComposition(composition.getId());

        //Then
        assertTrue(commentRepository.existsById(comment.getId()));
        assertEquals(1, savedCommentList.size());
        assertEquals("test comment description", savedCommentList.get(0).getDescription());
        assertNotNull(savedCommentList.get(0).getCreated());
        assertNotNull(savedCommentList.get(0).getComposition());
        assertTrue(savedCommentList.get(0).getReactionList().isEmpty());
    }

    @Test
    void testGetCommentsForComposition_ShouldGetEmptyList() throws ComponentExistsException, ComponentNotFoundException, IdException {
        //Given
        compositionService.saveComposition(composition);

        //When
        List<Comment> commentList = commentService.getCommentsForComposition(composition.getId());

        //Then
        assertTrue(commentList.isEmpty());
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
        assertTrue(commentRepository.existsById(commentId));
        assertEquals("test comment description", savedComment.getDescription());
        assertNotNull(savedComment.getCreated());
        assertNotNull(savedComment.getComposition());
        assertTrue(savedComment.getReactionList().isEmpty());
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

        //Then
        assertTrue(commentRepository.existsById(comment.getId()));
    }

    @Test
    void testSaveComment_ShouldThrowIdException() {
        //Given
        ReflectionTestUtils.setField(comment, "id", 1L);

        //When & Then
        assertThrows(IdException.class, () -> commentService.saveComment(comment));
    }

    @Test
    void testUpdateComment() throws ComponentExistsException, IdException, ComponentNotFoundException {
        //Given
        compositionService.saveComposition(composition);
        commentService.saveComment(comment);
        Comment descriptionUpdatedComment = Comment.builder()
                .id(comment.getId()).description("test updated description").created(LocalDateTime.now())
                .composition(composition).reactionList(List.of())
                .build();

        //When
        commentService.updateComment(descriptionUpdatedComment);

        //Then
        assertEquals("test updated description", comment.getDescription());
    }

    @Test
    void testUpdateComment_ShouldThrowIdException() {
        //When & Then
        assertThrows(IdException.class, () -> commentService.updateComment(comment));
    }
}
