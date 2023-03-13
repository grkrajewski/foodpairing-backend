package com.myapp.foodpairingbackend.facade;

import com.myapp.foodpairingbackend.domain.dto.CommentDto;
import com.myapp.foodpairingbackend.domain.entity.Comment;
import com.myapp.foodpairingbackend.exception.CommentNotFoundException;
import com.myapp.foodpairingbackend.exception.CompositionNotFoundException;
import com.myapp.foodpairingbackend.exception.IdFoundException;
import com.myapp.foodpairingbackend.exception.IdNotFoundException;
import com.myapp.foodpairingbackend.mapper.CommentMapper;
import com.myapp.foodpairingbackend.service.CommentService;
import lombok.RequiredArgsConstructor;;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class CommentFacade {

    private final CommentService commentService;
    private final CommentMapper commentMapper;

    public List<CommentDto> getComments() {
        List<Comment> commentList = commentService.getComments();
        return commentMapper.mapToCommentDtoList(commentList);
    }

    public List<CommentDto> getCommentsForComposition(Long compositionId) {
        List<Comment> commentList = commentService.getCommentsForComposition(compositionId);
        return commentMapper.mapToCommentDtoList(commentList);
    }

    public CommentDto getComment(Long commentId) throws CommentNotFoundException {
        Comment comment = commentService.getComment(commentId);
        return commentMapper.mapToCommentDto(comment);
    }

    public void deleteComment(Long commentId) throws CommentNotFoundException {
        try {
            commentService.deleteComment(commentId);
        } catch (DataAccessException e) {
            throw new CommentNotFoundException();
        }
    }

    public CommentDto saveComment(CommentDto commentDto) throws CompositionNotFoundException, CommentNotFoundException,
            IdFoundException {
        if (commentDto.getId() == null) {
            Comment comment = commentMapper.mapToComment(commentDto);
            Comment savedComment = commentService.saveComment(comment);
            return commentMapper.mapToCommentDto(savedComment);
        }
        throw new IdFoundException();
    }

    public CommentDto updateComment(CommentDto commentDto) throws CompositionNotFoundException, CommentNotFoundException,
            IdNotFoundException {
        if (commentDto.getId() != null && commentService.getComment(commentDto.getId()) != null) {
            Comment comment = commentMapper.mapToComment(commentDto);
            Comment savedComment = commentService.saveComment(comment);
            return commentMapper.mapToCommentDto(savedComment);
        }
        throw new IdNotFoundException();
    }
}
