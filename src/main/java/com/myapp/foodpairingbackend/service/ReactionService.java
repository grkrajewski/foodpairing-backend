package com.myapp.foodpairingbackend.service;

import com.myapp.foodpairingbackend.domain.entity.Reaction;
import com.myapp.foodpairingbackend.exception.ComponentNotFoundException;
import com.myapp.foodpairingbackend.repository.CommentRepository;
import com.myapp.foodpairingbackend.repository.ReactionRepository;
import lombok.RequiredArgsConstructor;
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

    public void deleteReaction(final Long reactionId) {
        reactionRepository.deleteById(reactionId);
    }

    public Reaction saveReaction(final Reaction reaction) {
        return reactionRepository.save(reaction);
    }
}
