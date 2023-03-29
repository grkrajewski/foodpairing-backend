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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@Transactional
@SpringBootTest
class CommentServiceTest {

    @Autowired
    private CompositionService compositionService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private CommentRepository commentRepository;

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
            .id(null).dish(dish).drink(drink).created(new Date()).commentList(List.of())
            .build();

    Comment comment = Comment.builder().id(null).description("test description").created(new Date())
            .composition(composition).reactionList(List.of())
            .build();

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
    void testSaveComment() throws ComponentExistsException, IdException {
        //Given
        compositionService.saveComposition(composition);

        //When
        commentService.saveComment(comment);
        Long commentId = comment.getId();

        //Then
        assertTrue(commentRepository.existsById(commentId));
    }
}