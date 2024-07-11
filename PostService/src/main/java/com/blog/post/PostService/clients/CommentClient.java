package com.blog.post.PostService.clients;

import com.blog.post.PostService.payload.CommentDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "COMMENTSERVICE")
public interface CommentClient {
    @GetMapping("/comments")
    List<CommentDto> getComments(@RequestParam("postId") Long id);

    @DeleteMapping("/api/comments/{id}")
    String deleteComments(@PathVariable("id") long commentId);
}
