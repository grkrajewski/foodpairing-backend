package com.myapp.foodpairingbackend.service;

import com.myapp.foodpairingbackend.domain.entity.Reaction;
import com.myapp.foodpairingbackend.repository.ReactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReactionService {

    private final ReactionRepository reactionRepository;

    public List<Reaction> getReactions() {
        return reactionRepository.findAll();
    }

    public List<Reaction> getReactionsForComment(final Long commentId) {
        return reactionRepository.findByCommentId(commentId);
    }

    public void deleteReaction(final Long reactionId) {
        reactionRepository.deleteById(reactionId);
    }

    public Reaction saveReaction(final Reaction reaction) {
        return reactionRepository.save(reaction);
    }
}
