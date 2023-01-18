package com.myapp.foodpairingbackend.service;

import com.myapp.foodpairingbackend.domain.entity.Comment;
import com.myapp.foodpairingbackend.domain.entity.Composition;
import com.myapp.foodpairingbackend.domain.entity.Dish;
import com.myapp.foodpairingbackend.domain.entity.Drink;
import com.myapp.foodpairingbackend.exception.DrinkExistsException;
import com.myapp.foodpairingbackend.repository.CommentRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
@SpringBootTest
class CommentServiceTest {

    @Autowired
    private CompositionService compositionService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private CommentRepository commentRepository;

    @Test
    void testGetCommentsForComposition() throws DrinkExistsException {
        //Given
        Dish dish = Dish.builder()
                .id(null)
                .externalSystemId(1L).name("test name dish").readyInMinutes(10).servings(4)
                .recipeUrl("https://test.com").compositionList(new ArrayList<>())
                .build();

        Drink drink = Drink.builder()
                .id(null).externalSystemId("2").name("test name drink").alcoholic("test alcoholic")
                .glass("test glass").instructions("test instructions").drinkIngredientList(new ArrayList<>())
                .build();

        Composition composition = Composition.builder()
                .id(null).dish(dish).drink(drink).created(new Date()).commentList(new ArrayList<>())
                .build();

        compositionService.saveComposition(composition);

        Comment comment1 = Comment.builder().id(null).description("test description 1").created(new Date())
                .composition(composition).reactionList(new ArrayList<>())
                .build();

        Comment comment2 = Comment.builder().id(null).description("test description 2").created(new Date())
                .composition(composition).reactionList(new ArrayList<>())
                .build();

        Comment savedComment1 = commentService.saveComment(comment1);
        Comment savedComment2 = commentService.saveComment(comment2);
        Long savedCompositionId = composition.getId();
        Long comment1Id = savedComment1.getId();
        Long comment2Id = savedComment2.getId();

        //When
        List<Comment> savedCommentList = commentService.getCommentsForComposition(savedCompositionId);

        //Then
        assertTrue(commentRepository.existsById(comment1Id));
        assertTrue(commentRepository.existsById(comment2Id));
        assertEquals(2, savedCommentList.size());
    }

    @Test
    void testDeleteComment() throws DrinkExistsException {
        //Given
        Dish dish = Dish.builder()
                .id(null)
                .externalSystemId(1L).name("test name dish").readyInMinutes(10).servings(4)
                .recipeUrl("https://test.com").compositionList(new ArrayList<>())
                .build();

        Drink drink = Drink.builder()
                .id(null).externalSystemId("2").name("test name drink").alcoholic("test alcoholic")
                .glass("test glass").instructions("test instructions").drinkIngredientList(new ArrayList<>())
                .build();

        Composition composition = Composition.builder()
                .id(null).dish(dish).drink(drink).created(new Date()).commentList(new ArrayList<>())
                .build();

        compositionService.saveComposition(composition);

        Comment comment1 = Comment.builder().id(null).description("test description 1").created(new Date())
                .composition(composition).reactionList(new ArrayList<>())
                .build();

        Comment savedComment1 = commentService.saveComment(comment1);
        Long comment1Id = savedComment1.getId();

        //When
        commentService.deleteComment(comment1Id);

        //Then
        assertFalse(commentRepository.existsById(comment1Id));
    }

}