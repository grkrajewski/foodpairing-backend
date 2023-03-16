package com.myapp.foodpairingbackend.facade;

import com.myapp.foodpairingbackend.domain.dto.CommentDto;
import com.myapp.foodpairingbackend.domain.entity.Comment;
import com.myapp.foodpairingbackend.exception.ComponentNotFoundException;
import com.myapp.foodpairingbackend.exception.IdException;
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

    public CommentDto getComment(Long commentId) throws ComponentNotFoundException {
        Comment comment = commentService.getComment(commentId);
        return commentMapper.mapToCommentDto(comment);
    }

    public void deleteComment(Long commentId) throws ComponentNotFoundException {
        try {
            commentService.deleteComment(commentId);
        } catch (DataAccessException e) {
            throw new ComponentNotFoundException(ComponentNotFoundException.COMMENT);
        }
    }

    public CommentDto saveComment(CommentDto commentDto) throws ComponentNotFoundException, IdException {
        if (commentDto.getId() == null) {
            Comment comment = commentMapper.mapToComment(commentDto);
            Comment savedComment = commentService.saveComment(comment);
            return commentMapper.mapToCommentDto(savedComment);
        }
        throw new IdException(IdException.ID_FOUND);
    }

    public CommentDto updateComment(CommentDto commentDto) throws ComponentNotFoundException, IdException {
        if (commentDto.getId() != null && commentService.getComment(commentDto.getId()) != null) {
            Comment comment = commentMapper.mapToComment(commentDto);
            Comment savedComment = commentService.saveComment(comment);
            return commentMapper.mapToCommentDto(savedComment);
        }
        throw new IdException(IdException.ID_NOT_FOUND);
    }
}
