package com.myapp.foodpairingbackend.domain.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.Date;

@Builder
@Getter
public class ReactionDto {

    private Long id;
    private String description;
    private Date created;
    private Long commentId;
}
