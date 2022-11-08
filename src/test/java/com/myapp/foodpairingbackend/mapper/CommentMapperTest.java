package com.myapp.foodpairingbackend.mapper;

import com.myapp.foodpairingbackend.domain.dto.CommentDto;
import com.myapp.foodpairingbackend.domain.entity.Comment;
import com.myapp.foodpairingbackend.domain.entity.Composition;
import com.myapp.foodpairingbackend.service.CompositionService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
class CommentMapperTest {

    @Autowired
    private CommentMapper commentMapper;

    @MockBean
    private CompositionService compositionService;

    @Test
    void testMapToComment() throws Exception {
        //Given
        Composition composition = Composition.builder().id(1L).dish(null)
                .drink(null).created(null).commentList(List.of())
                .build();
        CommentDto commentDto = CommentDto.builder().id(2L).description("test description")
                .created(null).compositionId(1L).reactionList(List.of())
                .build();
        when(compositionService.getComposition(commentDto.getCompositionId())).thenReturn(composition);

        //When
        Comment comment = commentMapper.mapToComment(commentDto);

        //Then
        assertEquals(2L, comment.getId());
        assertEquals("test description", comment.getDescription());
        assertEquals(1L, comment.getComposition().getId());
        assertEquals(0, comment.getReactionList().size());
    }

    @Test
    void testMapToCommentDto() throws Exception {
        //Given
        Composition composition = Composition.builder().id(1L).dish(null)
                .drink(null).created(null).commentList(List.of())
                .build();
        Comment comment = Comment.builder().id(2L).description("test description")
                .created(null).composition(composition).reactionList(List.of())
                .build();

        //When
        CommentDto commentDto = commentMapper.mapToCommentDto(comment);

        //Then
        assertEquals(2L, commentDto.getId());
        assertEquals("test description", commentDto.getDescription());
        assertEquals(1L, commentDto.getCompositionId());
        assertEquals(0, commentDto.getReactionList().size());
    }

    @Test
    void testMapToCommentList() throws Exception {
        //Given
        Composition composition = Composition.builder().id(1L).dish(null)
                .drink(null).created(null).commentList(List.of())
                .build();
        CommentDto commentDto = CommentDto.builder().id(2L).description("test description")
                .created(null).compositionId(1L).reactionList(List.of())
                .build();
        List<CommentDto> commentDtoList = List.of(commentDto);
        when(compositionService.getComposition(commentDto.getCompositionId())).thenReturn(composition);

        //When
        List<Comment> commentList = commentMapper.mapToCommentList(commentDtoList);

        //Then
        assertEquals(1, commentList.size());
        assertEquals(2L, commentList.get(0).getId());
        assertEquals("test description", commentList.get(0).getDescription());
        assertEquals(1L, commentList.get(0).getComposition().getId());
        assertEquals(0, commentList.get(0).getReactionList().size());
    }

    @Test
    void testMapToCommentDtoList() throws Exception {
        //Given
        Composition composition = Composition.builder().id(1L).dish(null)
                .drink(null).created(null).commentList(List.of())
                .build();
        Comment comment = Comment.builder().id(2L).description("test description")
                .created(null).composition(composition).reactionList(List.of())
                .build();
        List<Comment> commentList = List.of(comment);

        //When
        List<CommentDto> commentDtoList = commentMapper.mapToCommentDtoList(commentList);

        //Then
        assertEquals(1, commentDtoList.size());
        assertEquals(2L, commentDtoList.get(0).getId());
        assertEquals("test description", commentDtoList.get(0).getDescription());
        assertEquals(1L, commentDtoList.get(0).getCompositionId());
        assertEquals(0, commentDtoList.get(0).getReactionList().size());
    }

}