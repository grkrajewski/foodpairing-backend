package com.myapp.foodpairingbackend.service;

import com.myapp.foodpairingbackend.domain.entity.Reaction;
import com.myapp.foodpairingbackend.exception.ComponentNotFoundException;
import com.myapp.foodpairingbackend.exception.IdException;
import com.myapp.foodpairingbackend.repository.CommentRepository;
import com.myapp.foodpairingbackend.repository.ReactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReactionService {

    private final ReactionRepository reactionRepository;
    private final CommentRepository commentRepository;

    public List<Reaction> getReactions() {
        return reactionRepository.findAll();
    }

    public List<Reaction> getReactionsForComment(final Long commentId) throws ComponentNotFoundException {
        if (commentRepository.existsById(commentId)) {
            return reactionRepository.findByCommentId(commentId);
        }
        throw new ComponentNotFoundException(ComponentNotFoundException.COMMENT);
    }

    public Reaction getReaction(final Long reactionId) throws ComponentNotFoundException {
        return reactionRepository.findById(reactionId)
                .orElseThrow(() -> new ComponentNotFoundException(ComponentNotFoundException.REACTION));
    }

    public void deleteReaction(final Long reactionId) throws ComponentNotFoundException {
        try {
            reactionRepository.deleteById(reactionId);
        } catch (DataAccessException e) {
            throw new ComponentNotFoundException(ComponentNotFoundException.REACTION);
        }
    }

    public Reaction saveReaction(final Reaction reaction) throws IdException {
        if (reaction.getId() == null) {
            return reactionRepository.save(reaction);
        }
        throw new IdException(IdException.ID_FOUND);
    }

    public Reaction updateReaction(final Reaction reaction) throws ComponentNotFoundException, IdException {
        if (reaction.getId() != null && getReaction(reaction.getId()) != null) {
            return reactionRepository.save(reaction);
        }
        throw new IdException(IdException.ID_NOT_FOUND);
    }
}
