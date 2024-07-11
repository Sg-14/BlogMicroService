package com.blog.post.PostService.controller;

import com.blog.post.PostService.payload.PostCommentDto;
import com.blog.post.PostService.payload.PostDto;
import com.blog.post.PostService.payload.PostResponse;
import com.blog.post.PostService.service.PostService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/posts")
public class PostController {

    @Autowired
    private PostService postService;

    @GetMapping("")
    public ResponseEntity<PostResponse> getAllPosts(
            @RequestParam(value = "pageNo", required = false, defaultValue = "0") int pageNo,
            @RequestParam(value = "pageSize", required = false, defaultValue = "10") int pageSize,
            @RequestParam(value = "sortBy", required = false, defaultValue = "id") String sortBy,
            @RequestParam(value = "sortDir", required = false, defaultValue = "asc") String sortDir
    ){
        return ResponseEntity.ok(postService.getAllPosts(pageNo, pageSize, sortBy, sortDir));
    }

    @GetMapping("{id}")
    public ResponseEntity<PostCommentDto> getPost(@PathVariable long id){
        return ResponseEntity.ok(postService.getPost(id));
    }

    @PostMapping("")
    public ResponseEntity<PostDto> createPost(@Valid @RequestBody PostDto postDto){
        return new ResponseEntity<>(postService.createPost(postDto), HttpStatus.CREATED);
    }

    @PutMapping("{id}")
    public ResponseEntity<PostDto> updatePost(@Valid @RequestBody PostDto postDto, @PathVariable long id){
        PostDto postResponse = postService.updatePost(postDto, id);
        return ResponseEntity.ok(postResponse);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<String> deletePost(@PathVariable long id){
        postService.deletePost(id);
        return ResponseEntity.ok("Post Deleted!");
    }

    @GetMapping("categories/{categoryId}")
    public ResponseEntity<List<PostDto>> getPostsByCategoryId(@PathVariable long categoryId){
        return ResponseEntity.ok(postService.getPostsByCategory(categoryId));
    }
}
