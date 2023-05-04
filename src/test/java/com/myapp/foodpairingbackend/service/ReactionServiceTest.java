package com.myapp.foodpairingbackend.service;

import com.myapp.foodpairingbackend.domain.entity.*;
import com.myapp.foodpairingbackend.exception.ComponentExistsException;
import com.myapp.foodpairingbackend.exception.ComponentNotFoundException;
import com.myapp.foodpairingbackend.exception.IdException;
import com.myapp.foodpairingbackend.repository.ReactionRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

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

    @InjectMocks
    private ReactionService reactionServiceMock;

    @Mock
    private ReactionRepository reactionRepositoryMock;

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

    Comment comment = Comment.builder()
            .id(null).description("test comment description").created(LocalDateTime.now())
            .composition(composition).reactionList(List.of())
            .build();

    Reaction reaction = Reaction.builder()
            .id(null).description("test reaction description").created(LocalDateTime.now()).comment(comment)
            .build();

    @Test
    void testGetReactions() {
        //Given
        when(reactionRepositoryMock.findAll()).thenReturn(List.of(reaction));

        //When
        List<Reaction> reactions = reactionServiceMock.getReactions();

        //Then
        assertEquals(1, reactions.size());
        verify(reactionRepositoryMock, times(1)).findAll();
    }

    @Test
    void testGetReactions_ShouldFetchEmptyList() {
        //When
        List<Reaction> reactions = reactionServiceMock.getReactions();

        //Then
        assertEquals(0, reactions.size());
        verify(reactionRepositoryMock, times(1)).findAll();
    }

    @Test
    void testGetReactionsForComment() throws ComponentExistsException, ComponentNotFoundException, IdException {
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
    void testGetReactionsForComment_ShouldGetEmptyList() throws ComponentExistsException, ComponentNotFoundException, IdException {
        //Given
        compositionService.saveComposition(composition);
        commentService.saveComment(comment);
        Long commentId = comment.getId();

        //When
        List<Reaction> reactionList = reactionService.getReactionsForComment(commentId);

        //Then
        assertEquals(0, reactionList.size());
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
        Long reactionId = reaction.getId();

        //Then
        assertTrue(reactionRepository.existsById(reactionId));
    }

    @Test
    void testSaveReaction_ShouldThrowIdException() {
        //Given
        Reaction reactionWithId = Reaction.builder()
                .id(1L).description("test reaction description").created(LocalDateTime.now()).comment(comment)
                .build();

        //When & Then
        assertThrows(IdException.class, () -> reactionService.saveReaction(reactionWithId));
    }

    @Test
    void testUpdateReaction() throws ComponentExistsException, IdException, ComponentNotFoundException {
        //Given
        compositionService.saveComposition(composition);
        commentService.saveComment(comment);
        reactionService.saveReaction(reaction);
        Long reactionId = reaction.getId();
        Reaction descriptionUpdatedReaction = Reaction.builder()
                .id(reactionId).description("test updated reaction description").created(LocalDateTime.now()).comment(comment)
                .build();

        //When
        Reaction updatedReaction = reactionService.updateReaction(descriptionUpdatedReaction);

        //Then
        assertEquals("test updated reaction description", updatedReaction.getDescription());
    }

    @Test
    void testUpdateReaction_ShouldThrowIdException() {
        //When & Then
        assertThrows(IdException.class, () -> reactionService.updateReaction(reaction));
    }
}