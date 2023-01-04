package com.myapp.foodpairingbackend.domain.dataprovider;

import com.myapp.foodpairingbackend.domain.dto.CommentDto;
import com.myapp.foodpairingbackend.domain.entity.Composition;
import com.myapp.foodpairingbackend.exception.CompositionNotFoundException;
import com.myapp.foodpairingbackend.service.CompositionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CompositionProvider {

    private final CompositionService compositionService;

    public Composition fetchComposition(final CommentDto commentDto) throws CompositionNotFoundException {
        return compositionService.getComposition(commentDto.getCompositionId());
    }
}
