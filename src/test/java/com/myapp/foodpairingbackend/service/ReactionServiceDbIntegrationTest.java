package com.myapp.foodpairingbackend.service;

import com.myapp.foodpairingbackend.domain.entity.*;
import com.myapp.foodpairingbackend.exception.ComponentExistsException;
import com.myapp.foodpairingbackend.exception.ComponentNotFoundException;
import com.myapp.foodpairingbackend.exception.IdException;
import com.myapp.foodpairingbackend.repository.ReactionRepository;
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
public class ReactionServiceDbIntegrationTest {

    @Autowired
    private CompositionService compositionService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private ReactionService reactionService;

    @Autowired
    private ReactionRepository reactionRepository;

    private Composition composition;
    private Comment comment;
    private Reaction reaction;


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

        reaction = Reaction.builder()
                .id(null).description("test reaction description").created(LocalDateTime.now()).comment(comment)
                .build();
    }

    @Test
    void testGetReactions() throws IdException, ComponentExistsException {
        //Given
        compositionService.saveComposition(composition);
        commentService.saveComment(comment);
        reactionService.saveReaction(reaction);

        //When
        List<Reaction> reactions = reactionService.getReactions();

        //Then
        assertEquals(1, reactions.size());
    }

    @Test
    void testGetReactions_ShouldFetchEmptyList() {
        //When
        List<Reaction> reactions = reactionService.getReactions();

        //Then
        assertTrue(reactions.isEmpty());
    }

    @Test
    void testGetReactionsForComment() throws ComponentExistsException, ComponentNotFoundException, IdException {
        //Given
        compositionService.saveComposition(composition);
        commentService.saveComment(comment);
        reactionService.saveReaction(reaction);

        //When
        List<Reaction> savedReactionList = reactionService.getReactionsForComment(comment.getId());

        //Then
        assertTrue(reactionRepository.existsById(reaction.getId()));
        assertEquals(1, savedReactionList.size());
        assertEquals("test reaction description", savedReactionList.get(0).getDescription());
        assertNotNull(savedReactionList.get(0).getCreated());
        assertEquals("test comment description", savedReactionList.get(0).getComment().getDescription());
    }

    @Test
    void testGetReactionsForComment_ShouldGetEmptyList() throws ComponentExistsException, ComponentNotFoundException, IdException {
        //Given
        compositionService.saveComposition(composition);
        commentService.saveComment(comment);

        //When
        List<Reaction> reactionList = reactionService.getReactionsForComment(comment.getId());

        //Then
        assertTrue(reactionList.isEmpty());
    }

    @Test
    void testGetReactionsForComment_ShouldThrowComponentNotFoundException() {
        //When & Then
        assertThrows(ComponentNotFoundException.class, () -> reactionService.getReactionsForComment(1L));
    }

    @Test
    void testGetReaction() throws ComponentExistsException, ComponentNotFoundException, IdException {
        //Given
        compositionService.saveComposition(composition);
        commentService.saveComment(comment);
        reactionService.saveReaction(reaction);

        //When
        Reaction savedReaction = reactionService.getReaction(reaction.getId());

        //Then
        assertEquals("test reaction description", savedReaction.getDescription());
        assertNotNull(savedReaction.getCreated());
        assertNotNull(savedReaction.getComment());
    }

    @Test
    void testDeleteReaction() throws ComponentExistsException, ComponentNotFoundException, IdException {
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
    void testDeleteReaction_ShouldThrowComponentNotFoundException() {
        //When & Then
        assertThrows(ComponentNotFoundException.class, () -> reactionService.deleteReaction(1L));
    }

    @Test
    void testSaveReaction() throws ComponentExistsException, IdException {
        //Given
        compositionService.saveComposition(composition);
        commentService.saveComment(comment);

        //When
        reactionService.saveReaction(reaction);

        //Then
        assertTrue(reactionRepository.existsById(reaction.getId()));
    }

    @Test
    void testSaveReaction_ShouldThrowIdException() {
        //Given
        ReflectionTestUtils.setField(reaction, "id", 1L);

        //When & Then
        assertThrows(IdException.class, () -> reactionService.saveReaction(reaction));
    }

    @Test
    void testUpdateReaction() throws ComponentExistsException, IdException, ComponentNotFoundException {
        //Given
        compositionService.saveComposition(composition);
        commentService.saveComment(comment);
        reactionService.saveReaction(reaction);
        Reaction descriptionUpdatedReaction = Reaction.builder()
                .id(reaction.getId()).description("test updated reaction description").created(LocalDateTime.now()).comment(comment)
                .build();

        //When
        reactionService.updateReaction(descriptionUpdatedReaction);

        //Then
        assertEquals("test updated reaction description", reaction.getDescription());
    }

    @Test
    void testUpdateReaction_ShouldThrowIdException() {
        //When & Then
        assertThrows(IdException.class, () -> reactionService.updateReaction(reaction));
    }
}
