package com.blog.post.PostService.clients;

import com.blog.post.PostService.payload.CategoryDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "CATEGORYSERVICE")
public interface CategoryClient {
    @GetMapping("/categories/{id}")
    CategoryDto getCategory(@PathVariable("id") Long id);


}
