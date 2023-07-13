package com.myapp.foodpairingbackend.facade;

import com.myapp.foodpairingbackend.domain.dto.ReactionDto;
import com.myapp.foodpairingbackend.domain.entity.*;
import com.myapp.foodpairingbackend.exception.ComponentNotFoundException;
import com.myapp.foodpairingbackend.exception.IdException;
import com.myapp.foodpairingbackend.mapper.ReactionMapper;
import com.myapp.foodpairingbackend.service.CommentService;
import com.myapp.foodpairingbackend.service.ReactionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@SpringBootTest
public class ReactionFacadeTest {

    @Autowired
    private ReactionFacade reactionFacade;

    @MockBean
    private ReactionService reactionService;

    @MockBean
    private CommentService commentService;

    @SpyBean
    private ReactionMapper reactionMapper;

    private Comment comment;
    private Reaction reaction;
    private ReactionDto reactionDto;

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
                .id(5L).description("test reaction description").created(LocalDateTime.of(2023, 7, 10, 13, 12, 33)).comment(comment)
                .build();

        reactionDto = ReactionDto.builder()
                .id(5L).description("test reaction description").created("2023-07-10 13:12:33").commentId(comment.getId())
                .build();
    }

    @Test
    void testGetReactions() {
        //Given
        when(reactionService.getReactions()).thenReturn(List.of(reaction));

        //When
        List<ReactionDto> reactions = reactionFacade.getReactions();

        //Then
        assertEquals(1, reactions.size());
        verify(reactionService, times(1)).getReactions();
        verify(reactionMapper, times(1)).mapToReactionDtoList(List.of(reaction));
    }

    @Test
    void testGetReactionsForComment() throws ComponentNotFoundException {
        //Given
        when(reactionService.getReactionsForComment(comment.getId())).thenReturn(List.of(reaction));

        //When
        List<ReactionDto> reactions = reactionFacade.getReactionsForComment(comment.getId());

        //Then
        assertEquals(1, reactions.size());
        verify(reactionService, times(1)).getReactionsForComment(4L);
        verify(reactionMapper, times(1)).mapToReactionDtoList(List.of(reaction));
    }

    @Test
    void testGetReaction() throws ComponentNotFoundException {
        //Given
        when(reactionService.getReaction(reaction.getId())).thenReturn(reaction);

        //When
        ReactionDto fetchedReactionDto = reactionFacade.getReaction(reaction.getId());

        //Then
        assertEquals(5L, fetchedReactionDto.getId());
        assertEquals("test reaction description", fetchedReactionDto.getDescription());
        assertEquals("2023-07-10 13:12:33", fetchedReactionDto.getCreated());
        assertEquals(4L, fetchedReactionDto.getCommentId());
        verify(reactionService, times(1)).getReaction(5L);
        verify(reactionMapper, times(1)).mapToReactionDto(reaction);
    }

    @Test
    void deleteReaction() throws ComponentNotFoundException {
        //Given
        doNothing().when(reactionService).deleteReaction(reaction.getId());

        //When
        reactionFacade.deleteReaction(reaction.getId());

        //Then
        verify(reactionService, times(1)).deleteReaction(5L);
    }

    @Test
    void testSaveReaction() throws IdException, ComponentNotFoundException {
        //Given
        when(reactionService.saveReaction(any(Reaction.class))).thenReturn(reaction);

        //When
        ReactionDto savedReactionDto = reactionFacade.saveReaction(reactionDto);

        //Then
        assertEquals(5L, savedReactionDto.getId());
        assertEquals("test reaction description", savedReactionDto.getDescription());
        assertEquals("2023-07-10 13:12:33", savedReactionDto.getCreated());
        assertEquals(4L, savedReactionDto.getCommentId());
        verify(reactionService, times(1)).saveReaction(any(Reaction.class));
        verify(reactionMapper, times(1)).mapToReactionDto(reaction);
        verify(reactionMapper, times(1)).mapToReaction(reactionDto);
    }

    @Test
    void testUpdateReaction() throws ComponentNotFoundException, IdException {
        //Given
        when(reactionService.updateReaction(any(Reaction.class))).thenAnswer(answer -> {
            ReflectionTestUtils.setField(reaction, "created",
                    LocalDateTime.of(2023, 7, 11, 10, 20, 30));
            return reaction;
        });

        //When
        ReactionDto updatedReactionDto = reactionFacade.updateReaction(reactionDto);

        //Then
        assertEquals(5L, updatedReactionDto.getId());
        assertEquals("test reaction description", updatedReactionDto.getDescription());
        assertEquals("2023-07-11 10:20:30", updatedReactionDto.getCreated());
        assertEquals(4L, updatedReactionDto.getCommentId());
        verify(reactionService, times(1)).updateReaction(any(Reaction.class));
        verify(reactionMapper, times(1)).mapToReactionDto(reaction);
        verify(reactionMapper, times(1)).mapToReaction(reactionDto);
    }
}
