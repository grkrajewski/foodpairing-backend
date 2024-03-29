package com.myapp.foodpairingbackend.domain.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class CompositionDto {

    private Long id;
    private Long dishId;
    private Long drinkId;
    private String created;
    private List<CommentDto> commentList;
}
