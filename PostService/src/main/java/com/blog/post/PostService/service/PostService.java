package com.blog.post.PostService.service;


//import com.springboot.blog.springboot_blog_rest_api.payload.PostResponse;

import com.blog.post.PostService.payload.PostCommentDto;
import com.blog.post.PostService.payload.PostDto;
import com.blog.post.PostService.payload.PostResponse;

import java.util.List;
import java.util.Map;

public interface PostService {
    public PostDto createPost(PostDto postDto);
    public PostResponse getAllPosts(int pageNo, int pageSize, String sortBy, String sortDir);
    public PostCommentDto getPost(long id, Map<String, String> header);
    public void deletePost(long id);
    public PostDto updatePost(PostDto postDto,long id);
    public List<PostDto> getPostsByCategory(long categoryId);
}