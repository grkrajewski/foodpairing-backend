package com.myapp.foodpairingbackend.service;

import com.myapp.foodpairingbackend.domain.entity.*;
import com.myapp.foodpairingbackend.repository.ReactionRepository;
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
class ReactionServiceTest {

    @Autowired
    private CompositionService compositionService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private ReactionService reactionService;

    @Autowired
    private ReactionRepository reactionRepository;

    @Test
    void testGetReactionsForComment() {
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

        Comment comment = Comment.builder().id(null).description("test description").created(new Date())
                .composition(composition).reactionList(new ArrayList<>())
                .build();

        commentService.saveComment(comment);

        Reaction reaction1 = Reaction.builder().id(1L).description("test description 1")
                .created(new Date()).comment(comment)
                .build();

        Reaction reaction2 = Reaction.builder().id(1L).description("test description 2")
                .created(new Date()).comment(comment)
                .build();

        Reaction savedReaction1 = reactionService.saveReaction(reaction1);
        Reaction savedReaction2 = reactionService.saveReaction(reaction2);
        Long savedCommentId = comment.getId();
        Long reaction1Id = savedReaction1.getId();
        Long reaction2Id = savedReaction2.getId();

        //When
        List<Reaction> savedReactionList = reactionService.getReactionsForComment(savedCommentId);

        //Then
        assertTrue(reactionRepository.existsById(reaction1Id));
        assertTrue(reactionRepository.existsById(reaction2Id));
        assertEquals(2, savedReactionList.size());
    }

    @Test
    void testDeleteReaction() {
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

        Comment comment = Comment.builder().id(null).description("test description").created(new Date())
                .composition(composition).reactionList(new ArrayList<>())
                .build();

        commentService.saveComment(comment);

        Reaction reaction1 = Reaction.builder().id(1L).description("test description 1")
                .created(new Date()).comment(comment)
                .build();

        Reaction savedReaction1 = reactionService.saveReaction(reaction1);
        Long reaction1Id = savedReaction1.getId();

        //When
        reactionService.deleteReaction(reaction1Id);

        //Then
        assertFalse(reactionRepository.existsById(reaction1Id));
    }
}