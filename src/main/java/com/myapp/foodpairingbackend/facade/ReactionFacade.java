package com.myapp.foodpairingbackend.facade;

import com.myapp.foodpairingbackend.domain.dto.ReactionDto;
import com.myapp.foodpairingbackend.domain.entity.Reaction;
import com.myapp.foodpairingbackend.exception.CommentNotFoundException;
import com.myapp.foodpairingbackend.exception.IdFoundException;
import com.myapp.foodpairingbackend.exception.IdNotFoundException;
import com.myapp.foodpairingbackend.mapper.ReactionMapper;
import com.myapp.foodpairingbackend.service.ReactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ReactionFacade {

    private final ReactionService reactionService;
    private final ReactionMapper reactionMapper;

    public List<ReactionDto> getReactions() {
        List<Reaction> reactionList = reactionService.getReactions();
        return reactionMapper.mapToReactionDtoList(reactionList);
    }

    public List<ReactionDto> getReactionsForComment(Long commentId) {
        List<Reaction> reactionList = reactionService.getReactionsForComment(commentId);
        return reactionMapper.mapToReactionDtoList(reactionList);
    }

    public void deleteReaction(Long reactionId) {
        reactionService.deleteReaction(reactionId);
    }

    public ReactionDto saveReaction(ReactionDto reactionDto) throws CommentNotFoundException, IdFoundException {
        if (reactionDto.getId() == null) {
            Reaction reaction = reactionMapper.mapToReaction(reactionDto);
            Reaction savedReaction = reactionService.saveReaction(reaction);
            return reactionMapper.mapToReactionDto(savedReaction);
        }
        throw new IdFoundException();
    }

    public ReactionDto updateReaction(ReactionDto reactionDto) throws CommentNotFoundException, IdNotFoundException {
        if (reactionDto.getId() != null) {
            Reaction reaction = reactionMapper.mapToReaction(reactionDto);
            Reaction savedReaction = reactionService.saveReaction(reaction);
            return reactionMapper.mapToReactionDto(savedReaction);
        }
        throw new IdNotFoundException();
    }
}
