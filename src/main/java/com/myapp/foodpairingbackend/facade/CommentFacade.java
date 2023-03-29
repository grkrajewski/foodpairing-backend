package com.myapp.foodpairingbackend.facade;

import com.myapp.foodpairingbackend.domain.dto.CommentDto;
import com.myapp.foodpairingbackend.domain.entity.Comment;
import com.myapp.foodpairingbackend.exception.ComponentNotFoundException;
import com.myapp.foodpairingbackend.exception.IdException;
import com.myapp.foodpairingbackend.mapper.CommentMapper;
import com.myapp.foodpairingbackend.service.CommentService;
import lombok.RequiredArgsConstructor;
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

    public List<CommentDto> getCommentsForComposition(Long compositionId) throws ComponentNotFoundException {
        List<Comment> commentList = commentService.getCommentsForComposition(compositionId);
        return commentMapper.mapToCommentDtoList(commentList);
    }

    public CommentDto getComment(Long commentId) throws ComponentNotFoundException {
        Comment comment = commentService.getComment(commentId);
        return commentMapper.mapToCommentDto(comment);
    }

    public void deleteComment(Long commentId) throws ComponentNotFoundException {
        commentService.deleteComment(commentId);
    }

    public CommentDto saveComment(CommentDto commentDto) throws ComponentNotFoundException, IdException {
        Comment comment = commentMapper.mapToComment(commentDto);
        Comment savedComment = commentService.saveComment(comment);
        return commentMapper.mapToCommentDto(savedComment);
    }

    public CommentDto updateComment(CommentDto commentDto) throws ComponentNotFoundException, IdException {
        Comment comment = commentMapper.mapToComment(commentDto);
        Comment savedComment = commentService.updateComment(comment);
        return commentMapper.mapToCommentDto(savedComment);
    }
}
