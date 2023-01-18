package com.myapp.foodpairingbackend.service;

import com.myapp.foodpairingbackend.domain.entity.Composition;
import com.myapp.foodpairingbackend.exception.CompositionNotFoundException;
import com.myapp.foodpairingbackend.exception.DrinkExistsException;
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

    public Composition getComposition(final Long compositionId) throws CompositionNotFoundException {
        return compositionRepository.findById(compositionId).orElseThrow(CompositionNotFoundException::new);
    }

    public void deleteComposition(final Long compositionId) {
        compositionRepository.deleteById(compositionId);
    }

    public Composition saveComposition(final Composition composition) throws DrinkExistsException {
        List<Composition> compositionList = compositionRepository.findAll();
        for (Composition existingComposition : compositionList) {
            if (existingComposition.getDrink().getId().equals(composition.getDrink().getId())
                    && !existingComposition.getId().equals(composition.getId())) {
                throw new DrinkExistsException();
            }
        }
        return compositionRepository.save(composition);
    }
}
