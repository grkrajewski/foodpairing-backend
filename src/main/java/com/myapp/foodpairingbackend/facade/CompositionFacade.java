package com.myapp.foodpairingbackend.facade;

import com.myapp.foodpairingbackend.domain.dto.CompositionDto;
import com.myapp.foodpairingbackend.domain.entity.Composition;
import com.myapp.foodpairingbackend.exception.*;
import com.myapp.foodpairingbackend.mapper.CompositionMapper;
import com.myapp.foodpairingbackend.service.CompositionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class CompositionFacade {

    private final CompositionService compositionService;
    private final CompositionMapper compositionMapper;

    public List<CompositionDto> getCompositions() {
        List<Composition> compositionList = compositionService.getCompositions();
        return compositionMapper.mapToCompositionDtoList(compositionList);
    }

    public CompositionDto getComposition(Long compositionId) throws ComponentNotFoundException {
        Composition composition = compositionService.getComposition(compositionId);
        return compositionMapper.mapToCompositionDto(composition);
    }

    public void deleteComposition(Long compositionId) throws ComponentNotFoundException {
        compositionService.deleteComposition(compositionId);
    }

    public CompositionDto saveComposition(CompositionDto compositionDto) throws ComponentNotFoundException, IdException,
            ComponentExistsException {
        Composition composition = compositionMapper.mapToComposition(compositionDto);
        Composition savedComposition = compositionService.saveComposition(composition);
        return compositionMapper.mapToCompositionDto(savedComposition);
    }

    public CompositionDto updateComposition(CompositionDto compositionDto) throws ComponentNotFoundException, IdException,
            ComponentExistsException {
        Composition composition = compositionMapper.mapToComposition(compositionDto);
        Composition savedComposition = compositionService.updateComposition(composition);
        return compositionMapper.mapToCompositionDto(savedComposition);
    }
}