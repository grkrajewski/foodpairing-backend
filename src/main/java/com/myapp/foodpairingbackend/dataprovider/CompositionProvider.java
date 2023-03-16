package com.myapp.foodpairingbackend.dataprovider;

import com.myapp.foodpairingbackend.domain.dto.CommentDto;
import com.myapp.foodpairingbackend.domain.entity.Composition;
import com.myapp.foodpairingbackend.exception.ComponentNotFoundException;
import com.myapp.foodpairingbackend.service.CompositionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CompositionProvider {

    private final CompositionService compositionService;

    public Composition fetchComposition(final CommentDto commentDto) throws ComponentNotFoundException {
        return compositionService.getComposition(commentDto.getCompositionId());
    }
}
