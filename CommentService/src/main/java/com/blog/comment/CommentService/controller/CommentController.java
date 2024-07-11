package com.blog.comment.CommentService.controller;

import com.blog.comment.CommentService.payload.CommentDto;
import com.blog.comment.CommentService.service.CommentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/comments")
public class CommentController {

    @Autowired
    private CommentService commentService;


    @PostMapping("")
    public ResponseEntity<CommentDto> createComment(@RequestParam long postId, @Valid @RequestBody CommentDto commentDto){
        return new ResponseEntity<>(commentService.createComment(postId, commentDto), HttpStatus.CREATED);
    }

    @GetMapping("")
    public ResponseEntity<List<CommentDto>> getAllComments(
            @RequestParam long postId,
            @RequestParam(value = "pageNo", defaultValue = "0", required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = "id", required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = "asc", required = false) String sortDir
    ){
        return ResponseEntity.ok(commentService.getAllComments(postId));
    }

    @GetMapping("{commentId}")
    public ResponseEntity<CommentDto> getComment(@RequestParam long postId, @PathVariable long commentId){
        return ResponseEntity.ok(commentService.getComment(postId, commentId));
    }


    @DeleteMapping("{commentId}")
    public ResponseEntity<String> deleteComment(@PathVariable long commentId){
        commentService.deleteComment(commentId);
        return ResponseEntity.ok("Comment Deleted Successfully");
    }

    @PutMapping("{postId}/comments/{commentId}")
    public ResponseEntity<CommentDto> updateComment(
            @PathVariable long commentId,
            @Valid @RequestBody CommentDto commentDto){
        return ResponseEntity.ok(commentService.updateComment(commentId, commentDto));
    }
}