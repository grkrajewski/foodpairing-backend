package com.myapp.foodpairingbackend.service;

import com.myapp.foodpairingbackend.domain.entity.Comment;
import com.myapp.foodpairingbackend.exception.CommentNotFoundException;
import com.myapp.foodpairingbackend.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;

    public List<Comment> getComments() {
        return commentRepository.findAll();
    }

    public List<Comment> getCommentsForComposition(final Long compositionId) {
        List<Comment> allCommentList = commentRepository.findAll();
        List<Comment> filteredCommentList = new ArrayList<>();
        for (Comment comment : allCommentList) {
            if (comment.getComposition().getId().equals(compositionId)) {
                filteredCommentList.add(comment);
            }
        }
        return filteredCommentList;
    }

    public Comment getComment(final Long commentId) throws CommentNotFoundException {
        return commentRepository.findById(commentId).orElseThrow(CommentNotFoundException::new);
    }

    public void deleteComment(final Long commentId) {
        commentRepository.deleteById(commentId);
    }

    public Comment saveComment(final Comment comment) {
        return commentRepository.save(comment);
    }
}
