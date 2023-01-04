package com.myapp.foodpairingbackend.domain.dataprovider;

import com.myapp.foodpairingbackend.domain.dto.ReactionDto;
import com.myapp.foodpairingbackend.domain.entity.Comment;
import com.myapp.foodpairingbackend.exception.CommentNotFoundException;
import com.myapp.foodpairingbackend.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CommentProvider {

    private final CommentService commentService;

    public Comment fetchComment(final ReactionDto reactionDto) throws CommentNotFoundException {
        return commentService.getComment(reactionDto.getCommentId());
    }
}
