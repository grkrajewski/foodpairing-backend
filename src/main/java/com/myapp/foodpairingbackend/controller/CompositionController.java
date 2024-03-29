package com.myapp.foodpairingbackend.controller;

import com.myapp.foodpairingbackend.domain.dto.CompositionDto;
import com.myapp.foodpairingbackend.exception.*;
import com.myapp.foodpairingbackend.facade.CompositionFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/foodpairing/v1/compositions")
@RequiredArgsConstructor
public class CompositionController {

    private final CompositionFacade compositionFacade;

    @GetMapping
    public ResponseEntity<List<CompositionDto>> getCompositions() {
        return ResponseEntity.ok(compositionFacade.getCompositions());
    }

    @GetMapping(value = "{compositionId}")
    public ResponseEntity<CompositionDto> getComposition(@PathVariable Long compositionId) throws ComponentNotFoundException {
        return ResponseEntity.ok(compositionFacade.getComposition(compositionId));
    }

    @DeleteMapping(value = "{compositionId}")
    public ResponseEntity<Void> deleteComposition(@PathVariable Long compositionId) throws ComponentNotFoundException {
        compositionFacade.deleteComposition(compositionId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CompositionDto> saveComposition(@RequestBody CompositionDto compositionDto) throws ComponentNotFoundException,
            IdException, ComponentExistsException {
        CompositionDto savedCompositionDto = compositionFacade.saveComposition(compositionDto);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(savedCompositionDto.getId()).toUri();
        return ResponseEntity.created(location).body(savedCompositionDto);
    }

    @PutMapping
    public ResponseEntity<CompositionDto> updateComposition(@RequestBody CompositionDto compositionDto) throws ComponentNotFoundException,
            IdException, ComponentExistsException {
        return ResponseEntity.ok(compositionFacade.updateComposition(compositionDto));
    }
}
