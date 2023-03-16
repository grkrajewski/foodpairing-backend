package com.myapp.foodpairingbackend.service;

import com.myapp.foodpairingbackend.domain.entity.Composition;
import com.myapp.foodpairingbackend.exception.ComponentExistsException;
import com.myapp.foodpairingbackend.exception.ComponentNotFoundException;
import com.myapp.foodpairingbackend.repository.CompositionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CompositionService {

    private final CompositionRepository compositionRepository;

    public List<Composition> getCompositions() {
        return compositionRepository.findAll();
    }

    public Composition getComposition(final Long compositionId) throws ComponentNotFoundException {
        return compositionRepository.findById(compositionId).orElseThrow(() -> new ComponentNotFoundException(ComponentNotFoundException.COMPOSITION));
    }

    public void deleteComposition(final Long compositionId) {
        compositionRepository.deleteById(compositionId);
    }

    public Composition saveComposition(final Composition composition) throws ComponentExistsException {
        Composition existingComposition = compositionRepository.findByDrinkId(composition.getDrink().getId());
        if (existingComposition != null) {
            throw new ComponentExistsException(ComponentExistsException.DRINK_EXISTS);
        }
        return compositionRepository.save(composition);
    }
}
