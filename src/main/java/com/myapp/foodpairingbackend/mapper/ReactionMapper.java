package com.myapp.foodpairingbackend.mapper;

import com.myapp.foodpairingbackend.dataprovider.CommentProvider;
import com.myapp.foodpairingbackend.domain.dto.ReactionDto;
import com.myapp.foodpairingbackend.domain.entity.Reaction;
import com.myapp.foodpairingbackend.exception.ComponentNotFoundException;
import com.myapp.foodpairingbackend.utils.DateTimeConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReactionMapper {

    private final CommentProvider commentProvider;

    public Reaction mapToReaction(final ReactionDto reactionDto) throws ComponentNotFoundException {
        Reaction reaction = Reaction.builder()
                .id(reactionDto.getId())
                .description(reactionDto.getDescription())
                .created(DateTimeConverter.convertStringToLocalDateTime(reactionDto.getCreated()))
                .comment(commentProvider.fetchComment(reactionDto))
                .build();
        return reaction;
    }

    public ReactionDto mapToReactionDto(final Reaction reaction) {
        ReactionDto reactionDto = ReactionDto.builder()
                .id(reaction.getId())
                .description(reaction.getDescription())
                .created(DateTimeConverter.convertLocalDateTimeToString(reaction.getCreated()))
                .commentId(reaction.getComment().getId())
                .build();
        return reactionDto;
    }

    public List<Reaction> mapToReactionList(final List<ReactionDto> reactionDtoList) throws ComponentNotFoundException {
        List<Reaction> reactionList = new ArrayList<>();
        for (ReactionDto reactionDto : reactionDtoList) {
            Reaction reaction = mapToReaction(reactionDto);
            reactionList.add(reaction);
        }
        return reactionList;
    }

    public List<ReactionDto> mapToReactionDtoList(final List<Reaction> reactionList) {
        return reactionList.stream()
                .map(this::mapToReactionDto)
                .collect(Collectors.toList());
    }
}
