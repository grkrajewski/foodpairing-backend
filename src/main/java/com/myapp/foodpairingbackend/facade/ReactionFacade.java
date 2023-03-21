package com.myapp.foodpairingbackend.facade;

import com.myapp.foodpairingbackend.domain.dto.ReactionDto;
import com.myapp.foodpairingbackend.domain.entity.Reaction;
import com.myapp.foodpairingbackend.exception.ComponentNotFoundException;
import com.myapp.foodpairingbackend.exception.IdException;
import com.myapp.foodpairingbackend.mapper.ReactionMapper;
import com.myapp.foodpairingbackend.service.ReactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
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

    public List<ReactionDto> getReactionsForComment(Long commentId) throws ComponentNotFoundException {
        List<Reaction> reactionList = reactionService.getReactionsForComment(commentId);
        return reactionMapper.mapToReactionDtoList(reactionList);
    }

    public ReactionDto getReaction(Long reactionId) throws ComponentNotFoundException {
        Reaction reaction = reactionService.getReaction(reactionId);
        return reactionMapper.mapToReactionDto(reaction);
    }

    public void deleteReaction(Long reactionId) throws ComponentNotFoundException {
        try {
            reactionService.deleteReaction(reactionId);
        } catch (DataAccessException e) {
            throw new ComponentNotFoundException(ComponentNotFoundException.REACTION);
        }
    }

    public ReactionDto saveReaction(ReactionDto reactionDto) throws ComponentNotFoundException, IdException {
        if (reactionDto.getId() == null) {
            Reaction reaction = reactionMapper.mapToReaction(reactionDto);
            Reaction savedReaction = reactionService.saveReaction(reaction);
            return reactionMapper.mapToReactionDto(savedReaction);
        }
        throw new IdException(IdException.ID_FOUND);
    }

    public ReactionDto updateReaction(ReactionDto reactionDto) throws ComponentNotFoundException, IdException {
        if (reactionDto.getId() != null && reactionService.getReaction(reactionDto.getId()) != null) {
            Reaction reaction = reactionMapper.mapToReaction(reactionDto);
            Reaction savedReaction = reactionService.saveReaction(reaction);
            return reactionMapper.mapToReactionDto(savedReaction);
        }
        throw new IdException(IdException.ID_NOT_FOUND);
    }
}
