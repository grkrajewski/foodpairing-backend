package com.myapp.foodpairingbackend.controller;

import com.myapp.foodpairingbackend.domain.dto.CommentDto;
import com.myapp.foodpairingbackend.exception.CommentNotFoundException;
import com.myapp.foodpairingbackend.exception.CompositionNotFoundException;
import com.myapp.foodpairingbackend.facade.CommentFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping(value = "{compositionId}")
    public ResponseEntity<List<CommentDto>> getCommentsForComposition(@PathVariable Long compositionId) {
        return ResponseEntity.ok(commentFacade.getCommentsForComposition(compositionId));
    }

    @DeleteMapping(value = "{commentId}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long commentId) {
        commentFacade.deleteComment(commentId);
        return ResponseEntity.ok().build();
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CommentDto> saveComment(@RequestBody CommentDto commentDto) throws CompositionNotFoundException,
            CommentNotFoundException {
        return ResponseEntity.ok(commentFacade.saveComment(commentDto));
    }

    @PutMapping
    public ResponseEntity<CommentDto> updateComment(@RequestBody CommentDto commentDto) throws CompositionNotFoundException,
            CommentNotFoundException {
        return ResponseEntity.ok(commentFacade.updateComment(commentDto));
    }
}
