package com.myapp.foodpairingbackend.service;

import com.myapp.foodpairingbackend.domain.entity.*;
import com.myapp.foodpairingbackend.exception.ComponentNotFoundException;
import com.myapp.foodpairingbackend.exception.IdException;
import com.myapp.foodpairingbackend.repository.CommentRepository;
import com.myapp.foodpairingbackend.repository.ReactionRepository;
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
import static org.mockito.Mockito.*;

@SpringBootTest
class ReactionServiceTest {

    @Autowired
    private ReactionService reactionService;

    @MockBean
    private ReactionRepository reactionRepository;

    @MockBean
    private CommentRepository commentRepository;

    private Comment comment;
    private Reaction reaction;

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

        Composition composition = Composition.builder()
                .id(3L).dish(dish).drink(drink).created(LocalDateTime.now()).commentList(List.of())
                .build();

        comment = Comment.builder()
                .id(4L).description("test comment description").created(LocalDateTime.now())
                .composition(composition).reactionList(List.of())
                .build();

        reaction = Reaction.builder()
                .id(5L).description("test reaction description").created(LocalDateTime.now()).comment(comment)
                .build();
    }

    @Test
    void testGetReactions() {
        //Given
        when(reactionRepository.findAll()).thenReturn(List.of(reaction));

        //When
        List<Reaction> reactions = reactionService.getReactions();

        //Then
        assertEquals(1, reactions.size());
        verify(reactionRepository, times(1)).findAll();
    }

    @Test
    void testGetReactions_ShouldFetchEmptyList() {
        //Given
        when(reactionRepository.findAll()).thenReturn(List.of());

        //When
        List<Reaction> reactions = reactionService.getReactions();

        //Then
        assertTrue(reactions.isEmpty());
        verify(reactionRepository, times(1)).findAll();
    }

    @Test
    void testGetReactionsForComment() throws ComponentNotFoundException {
        //Given
        when(commentRepository.existsById(comment.getId())).thenReturn(true);
        when(reactionRepository.findByCommentId(comment.getId())).thenReturn(List.of(reaction));

        //When
        List<Reaction> savedReactionList = reactionService.getReactionsForComment(comment.getId());

        //Then
        assertEquals(1, savedReactionList.size());
        verify(reactionRepository, times(1)).findByCommentId(4L);
    }

    @Test
    void testGetReactionsForComment_ShouldGetEmptyList() throws ComponentNotFoundException {
        //Given
        when(commentRepository.existsById(comment.getId())).thenReturn(true);
        when(reactionRepository.findByCommentId(comment.getId())).thenReturn(List.of());

        //When
        List<Reaction> savedReactionList = reactionService.getReactionsForComment(comment.getId());

        //Then
        assertTrue(savedReactionList.isEmpty());
        verify(reactionRepository, times(1)).findByCommentId(4L);
    }

    @Test
    void testGetReactionsForComment_ShouldThrowComponentNotFoundException() {
        //Give
        when(commentRepository.existsById(4L)).thenReturn(false);

        //When & Then
        assertThrows(ComponentNotFoundException.class, () -> reactionService.getReactionsForComment(4L));
    }

    @Test
    void testGetReaction() throws ComponentNotFoundException {
        //Given
        when(reactionRepository.findById(reaction.getId())).thenReturn(Optional.ofNullable(reaction));

        //When
        Reaction savedReaction = reactionService.getReaction(reaction.getId());

        //Then
        assertEquals(5L, savedReaction.getId());
        assertEquals("test reaction description", savedReaction.getDescription());
        assertNotNull(savedReaction.getCreated());
        assertNotNull(savedReaction.getComment());
        verify(reactionRepository, times(1)).findById(5L);
    }

    @Test
    void testDeleteReaction() throws ComponentNotFoundException {
        //Given
        doNothing().when(reactionRepository).deleteById(reaction.getId());

        //When
        reactionService.deleteReaction(reaction.getId());

        //Then
        verify(reactionRepository, times(1)).deleteById(5L);
    }

    @Test
    void testSaveReaction() throws IdException {
        //Given
        ReflectionTestUtils.setField(reaction, "id", null);
        when(reactionRepository.save(reaction)).thenAnswer(answer -> {
            ReflectionTestUtils.setField(reaction, "id", 5L);
            return reaction;
        });

        //When
        Reaction savedReaction = reactionService.saveReaction(reaction);

        //Then
        assertEquals(5L, savedReaction.getId());
        assertEquals("test reaction description", savedReaction.getDescription());
        assertNotNull(savedReaction.getCreated());
        assertNotNull(savedReaction.getComment());
        verify(reactionRepository, times(1)).save(reaction);
    }

    @Test
    void testSaveReaction_ShouldThrowIdException() {
        //When & Then
        assertThrows(IdException.class, () -> reactionService.saveReaction(reaction));
    }

    @Test
    void testUpdateReaction() throws IdException, ComponentNotFoundException {
        //Given
        when(reactionRepository.findById(reaction.getId())).thenReturn(Optional.ofNullable(reaction));
        when(reactionRepository.save(reaction)).thenAnswer(answer -> {
            ReflectionTestUtils.setField(reaction, "description", "test updated reaction description");
            return reaction;
        });

        //When
        Reaction updatedReaction = reactionService.updateReaction(reaction);

        //Then
        assertEquals("test updated reaction description", updatedReaction.getDescription());
        verify(reactionRepository, times(1)).save(reaction);
    }

    @Test
    void testUpdateReaction_ShouldThrowIdException() {
        //Given
        ReflectionTestUtils.setField(reaction, "id", null);

        //When & Then
        assertThrows(IdException.class, () -> reactionService.updateReaction(reaction));
    }
}