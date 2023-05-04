package com.myapp.foodpairingbackend.domain.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ReactionDto {

    private Long id;
    private String description;
    private String created;
    private Long commentId;
}
