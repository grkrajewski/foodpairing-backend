package com.myapp.foodpairingbackend.domain.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.Date;
import java.util.List;

@Builder
@Getter
public class CommentDto {

    private Long id;
    private String description;
    private Date created;
    private Long compositionId;
    private List<ReactionDto> reactionList;
}
