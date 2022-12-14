package com.myapp.foodpairingbackend.mapper;

import com.myapp.foodpairingbackend.domain.dto.ReactionDto;
import com.myapp.foodpairingbackend.domain.entity.Comment;
import com.myapp.foodpairingbackend.domain.entity.Reaction;
import com.myapp.foodpairingbackend.service.CommentService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
class ReactionMapperTest {

    @Autowired
    private ReactionMapper reactionMapper;

    @MockBean
    private CommentService commentService;

    @Test
    void testMapToComment() throws Exception {
        //Given
        Comment comment = Comment.builder().id(1L).description("test comment description")
                .created(null).composition(null).reactionList(List.of())
                .build();
        ReactionDto reactionDto = ReactionDto.builder().id(2L).description("test reaction description")
                .created(null).commentId(1L)
                .build();
        when(commentService.getComment(reactionDto.getCommentId())).thenReturn(comment);

        //When
        Reaction reaction = reactionMapper.mapToReaction(reactionDto);

        //Then
        assertEquals(2L, reaction.getId());
        assertEquals("test reaction description", reaction.getDescription());
        assertEquals(1L, reaction.getComment().getId());
    }

    @Test
    void testMapToCommentDto() throws Exception {
        //Given
        Comment comment = Comment.builder().id(1L).description("test comment description")
                .created(null).composition(null).reactionList(List.of())
                .build();
        Reaction reaction = Reaction.builder().id(2L).description("test reaction description")
                .created(null).comment(comment)
                .build();

        //When
        ReactionDto reactionDto = reactionMapper.mapToReactionDto(reaction);

        //Then
        assertEquals(2L, reactionDto.getId());
        assertEquals("test reaction description", reactionDto.getDescription());
        assertEquals(1L, reactionDto.getCommentId());
    }

    @Test
    void testMapToCommentList() throws Exception {
        //Given
        Comment comment = Comment.builder().id(1L).description("test comment description")
                .created(null).composition(null).reactionList(List.of())
                .build();
        ReactionDto reactionDto = ReactionDto.builder().id(2L).description("test reaction description")
                .created(null).commentId(1L)
                .build();
        List<ReactionDto> reactionDtoList = List.of(reactionDto);
        when(commentService.getComment(reactionDto.getCommentId())).thenReturn(comment);

        //When
        List<Reaction> reactionList = reactionMapper.mapToReactionList(reactionDtoList);

        //Then
        assertEquals(1, reactionList.size());
        assertEquals(2L, reactionList.get(0).getId());
        assertEquals("test reaction description", reactionList.get(0).getDescription());
        assertEquals(1L, reactionList.get(0).getComment().getId());
    }

    @Test
    void testMapToCommentDtoList() throws Exception {
        //Given
        Comment comment = Comment.builder().id(1L).description("test comment description")
                .created(null).composition(null).reactionList(List.of())
                .build();
        Reaction reaction = Reaction.builder().id(2L).description("test reaction description")
                .created(null).comment(comment)
                .build();
        List<Reaction> reactionList = List.of(reaction);

        //When
        List<ReactionDto> reactionDtoList = reactionMapper.mapToReactionDtoList(reactionList);

        //Then
        assertEquals(1, reactionDtoList.size());
        assertEquals(2L, reactionDtoList.get(0).getId());
        assertEquals("test reaction description", reactionDtoList.get(0).getDescription());
        assertEquals(1L, reactionDtoList.get(0).getCommentId());
    }
}