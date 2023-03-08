package com.myapp.foodpairingbackend.service;

import com.myapp.foodpairingbackend.domain.entity.*;
import com.myapp.foodpairingbackend.exception.DrinkExistsException;
import com.myapp.foodpairingbackend.exception.ReactionNotFoundException;
import com.myapp.foodpairingbackend.repository.ReactionRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
@SpringBootTest
class ReactionServiceTest {

    @Autowired
    private CompositionService compositionService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private ReactionService reactionService;

    @Autowired
    private ReactionRepository reactionRepository;

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

    Comment comment = Comment.builder()
            .id(null).description("test comment description").created(new Date())
            .composition(composition).reactionList(List.of())
            .build();

    Reaction reaction = Reaction.builder()
            .id(null).description("test reaction description").created(new Date()).comment(comment)
            .build();

    @Test
    void testGetReactionsForComment() throws DrinkExistsException {
        //Given
        compositionService.saveComposition(composition);
        commentService.saveComment(comment);
        reactionService.saveReaction(reaction);
        Long commentId = comment.getId();
        Long reactionId = reaction.getId();

        //When
        List<Reaction> savedReactionList = reactionService.getReactionsForComment(commentId);

        //Then
        assertTrue(reactionRepository.existsById(reactionId));
        assertEquals(1, savedReactionList.size());
        assertEquals("test reaction description", savedReactionList.get(0).getDescription());
        assertNotNull(savedReactionList.get(0).getCreated());
        assertEquals("test comment description", savedReactionList.get(0).getComment().getDescription());
    }

    @Test
    void testGetReaction() throws DrinkExistsException, ReactionNotFoundException {
        //Given
        compositionService.saveComposition(composition);
        commentService.saveComment(comment);
        reactionService.saveReaction(reaction);
        Long reactionId = reaction.getId();

        //When
        Reaction savedReaction = reactionService.getReaction(reactionId);

        //Then
        assertTrue(reactionRepository.existsById(reactionId));
        assertEquals("test reaction description", savedReaction.getDescription());
        assertNotNull(savedReaction.getCreated());
        assertNotNull(savedReaction.getComment());
    }

    @Test
    void testDeleteReaction() throws DrinkExistsException {
        //Given
        compositionService.saveComposition(composition);
        commentService.saveComment(comment);
        reactionService.saveReaction(reaction);
        Long reactionId = reaction.getId();

        //When
        reactionService.deleteReaction(reactionId);

        //Then
        assertFalse(reactionRepository.existsById(reactionId));
    }

    @Test
    void testSaveReaction() throws DrinkExistsException {
        //Given
        compositionService.saveComposition(composition);
        commentService.saveComment(comment);

        //When
        reactionService.saveReaction(reaction);
        Long reactionId = reaction.getId();

        //Then
        assertTrue(reactionRepository.existsById(reactionId));
    }
}