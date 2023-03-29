package com.myapp.foodpairingbackend.service;

import com.myapp.foodpairingbackend.domain.entity.Comment;
import com.myapp.foodpairingbackend.exception.ComponentNotFoundException;
import com.myapp.foodpairingbackend.exception.IdException;
import com.myapp.foodpairingbackend.repository.CommentRepository;
import com.myapp.foodpairingbackend.repository.CompositionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final CompositionRepository compositionRepository;

    public List<Comment> getComments() {
        return commentRepository.findAll();
    }

    public List<Comment> getCommentsForComposition(final Long compositionId) throws ComponentNotFoundException {
        if (compositionRepository.existsById(compositionId)) {
            return commentRepository.findByCompositionId(compositionId);
        }
        throw new ComponentNotFoundException(ComponentNotFoundException.COMPOSITION);
    }

    public Comment getComment(final Long commentId) throws ComponentNotFoundException {
        return commentRepository.findById(commentId)
                .orElseThrow(() -> new ComponentNotFoundException(ComponentNotFoundException.COMMENT));
    }

    public void deleteComment(final Long commentId) throws ComponentNotFoundException {
        try {
            commentRepository.deleteById(commentId);
        } catch (DataAccessException e) {
            throw new ComponentNotFoundException(ComponentNotFoundException.COMMENT);
        }
    }

    public Comment saveComment(final Comment comment) throws IdException {
        if (comment.getId() == null) {
            return commentRepository.save(comment);
        }
        throw new IdException(IdException.ID_FOUND);
    }

    public Comment updateComment(final Comment comment) throws IdException, ComponentNotFoundException {
        if (comment.getId() != null && getComment(comment.getId()) != null) {
            return commentRepository.save(comment);
        }
        throw new IdException(IdException.ID_NOT_FOUND);
    }
}
