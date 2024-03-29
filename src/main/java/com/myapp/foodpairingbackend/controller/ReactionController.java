package com.myapp.foodpairingbackend.controller;

import com.myapp.foodpairingbackend.domain.dto.ReactionDto;
import com.myapp.foodpairingbackend.exception.ComponentNotFoundException;
import com.myapp.foodpairingbackend.exception.IdException;
import com.myapp.foodpairingbackend.facade.ReactionFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/foodpairing/v1/reactions")
@RequiredArgsConstructor
public class ReactionController {

    private final ReactionFacade reactionFacade;

    @GetMapping
    public ResponseEntity<List<ReactionDto>> getReactions() {
        return ResponseEntity.ok(reactionFacade.getReactions());
    }

    @GetMapping(value = "for-comment/{commentId}")
    public ResponseEntity<List<ReactionDto>> getReactionsForComment(@PathVariable Long commentId) throws ComponentNotFoundException {
        return ResponseEntity.ok(reactionFacade.getReactionsForComment(commentId));
    }

    @GetMapping("{reactionId}")
    public ResponseEntity<ReactionDto> getReaction(@PathVariable Long reactionId) throws ComponentNotFoundException {
        return ResponseEntity.ok(reactionFacade.getReaction(reactionId));
    }

    @DeleteMapping(value = "{reactionId}")
    public ResponseEntity<Void> deleteReaction(@PathVariable Long reactionId) throws ComponentNotFoundException {
        reactionFacade.deleteReaction(reactionId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ReactionDto> saveReaction(@RequestBody ReactionDto reactionDto) throws ComponentNotFoundException,
            IdException {
        ReactionDto savedReactionDto = reactionFacade.saveReaction(reactionDto);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(savedReactionDto.getId()).toUri();
        return ResponseEntity.created(location).body(savedReactionDto);
    }

    @PutMapping
    public ResponseEntity<ReactionDto> updateReaction(@RequestBody ReactionDto reactionDto) throws ComponentNotFoundException,
            IdException {
        return ResponseEntity.ok(reactionFacade.updateReaction(reactionDto));
    }
}
