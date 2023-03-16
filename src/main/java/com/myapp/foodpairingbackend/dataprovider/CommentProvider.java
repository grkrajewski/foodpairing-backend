package com.myapp.foodpairingbackend.dataprovider;

import com.myapp.foodpairingbackend.domain.dto.ReactionDto;
import com.myapp.foodpairingbackend.domain.entity.Comment;
import com.myapp.foodpairingbackend.exception.ComponentNotFoundException;
import com.myapp.foodpairingbackend.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CommentProvider {

    private final CommentService commentService;

    public Comment fetchComment(final ReactionDto reactionDto) throws ComponentNotFoundException {
        return commentService.getComment(reactionDto.getCommentId());
    }
}
