package com.myapp.foodpairingbackend.controller;

import com.myapp.foodpairingbackend.domain.dto.CommentDto;
import com.myapp.foodpairingbackend.exception.ComponentNotFoundException;
import com.myapp.foodpairingbackend.exception.IdException;
import com.myapp.foodpairingbackend.facade.CommentFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/foodpairing/v1/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentFacade commentFacade;

    @GetMapping
    public ResponseEntity<List<CommentDto>> getComments() {
        return ResponseEntity.ok(commentFacade.getComments());
    }

    @GetMapping(value = "for-composition/{compositionId}")
    public ResponseEntity<List<CommentDto>> getCommentsForComposition(@PathVariable Long compositionId) throws ComponentNotFoundException {
        return ResponseEntity.ok(commentFacade.getCommentsForComposition(compositionId));
    }

    @GetMapping(value = "{commentId}")
    public ResponseEntity<CommentDto> getComment(@PathVariable Long commentId) throws ComponentNotFoundException {
        return ResponseEntity.ok(commentFacade.getComment(commentId));
    }

    @DeleteMapping(value = "{commentId}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long commentId) throws ComponentNotFoundException {
        commentFacade.deleteComment(commentId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CommentDto> saveComment(@RequestBody CommentDto commentDto) throws ComponentNotFoundException, IdException {
        CommentDto savedCommentDto = commentFacade.saveComment(commentDto);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(savedCommentDto.getId()).toUri();
        return ResponseEntity.created(location).body(savedCommentDto);
    }

    @PutMapping
    public ResponseEntity<CommentDto> updateComment(@RequestBody CommentDto commentDto) throws ComponentNotFoundException, IdException {
        return ResponseEntity.ok(commentFacade.updateComment(commentDto));
    }
}
