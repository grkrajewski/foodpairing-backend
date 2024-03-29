package com.myapp.foodpairingbackend.mapper;

import com.myapp.foodpairingbackend.dataprovider.CompositionProvider;
import com.myapp.foodpairingbackend.domain.dto.CommentDto;
import com.myapp.foodpairingbackend.domain.entity.Comment;
import com.myapp.foodpairingbackend.exception.ComponentNotFoundException;
import com.myapp.foodpairingbackend.utils.DateTimeConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentMapper {

    private final CompositionProvider compositionProvider;
    private final ReactionMapper reactionMapper;

    public Comment mapToComment(final CommentDto commentDto) throws ComponentNotFoundException {
        Comment comment = Comment.builder()
                .id(commentDto.getId())
                .description(commentDto.getDescription())
                .created(DateTimeConverter.convertStringToLocalDateTime(commentDto.getCreated()))
                .composition(compositionProvider.fetchComposition(commentDto))
                .reactionList(reactionMapper.mapToReactionList(commentDto.getReactionList()))
                .build();
        return comment;
    }

    public CommentDto mapToCommentDto(final Comment comment) {
        CommentDto commentDto = CommentDto.builder()
                .id(comment.getId())
                .description(comment.getDescription())
                .created(DateTimeConverter.convertLocalDateTimeToString(comment.getCreated()))
                .compositionId(comment.getComposition().getId())
                .reactionList(reactionMapper.mapToReactionDtoList(comment.getReactionList()))
                .build();
        return commentDto;
    }

    public List<Comment> mapToCommentList(final List<CommentDto> commentDtoList) throws ComponentNotFoundException {
        List<Comment> commentList = new ArrayList<>();
        for (CommentDto commentDto : commentDtoList) {
            Comment comment = mapToComment(commentDto);
            commentList.add(comment);
        }
        return commentList;
    }

    public List<CommentDto> mapToCommentDtoList(final List<Comment> commentList) {
        return commentList.stream()
                .map(this::mapToCommentDto)
                .collect(Collectors.toList());
    }
}
