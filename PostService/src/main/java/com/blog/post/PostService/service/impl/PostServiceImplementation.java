package com.blog.post.PostService.service.impl;

import com.blog.post.PostService.clients.CategoryClient;
import com.blog.post.PostService.clients.CommentClient;
import com.blog.post.PostService.entity.Post;
import com.blog.post.PostService.payload.*;
import com.blog.post.PostService.repository.PostRepository;
import com.blog.post.PostService.service.PostService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Service
public class PostServiceImplementation implements PostService {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private ModelMapper mapper;

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    CommentClient commentClient;

    @Autowired
    CategoryClient categoryClient;

    @Override
    public PostDto createPost(PostDto postDto){
        Post post = dtoToPost(postDto);
//        post.setCategory(category);
        Post newPost = postRepository.save(post);
        PostDto postResponse = postToDto(newPost);

        return postResponse;
    }

    @Override
    public PostResponse getAllPosts(int pageNo, int pageSize, String sortBy, String sortDir){

        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
        Page<Post> page = postRepository.findAll(pageable);

        // get content for page
        List<Post> posts = page.getContent();

//        RestTemplate restTemplate = new RestTemplate();

        List<PostDto> postDtoStream = posts.stream().map(post -> postToDto(post)).toList();

        List<PostCommentDto> outputStream = new ArrayList<>();

        postDtoStream.stream().forEach(
                postDto -> {
                    List<CommentDto> comments = getComments(postDto.getId());
                    CategoryDto category = getCategory(postDto.getCategoryId());
                    String categoryName = category.getCategoryName();
                    PostCommentDto postCommentDto=
                            PostCommentDtoMapper.postCommentDtoMapper(postDto, comments, categoryName);
                    outputStream.add(postCommentDto);
                }
        );

        PostResponse postResponse = new PostResponse();

        postResponse.setContent(outputStream);
        postResponse.setPageNo(page.getNumber());
        postResponse.setPageSize(page.getSize());
        postResponse.setTotalPages(page.getTotalPages());
        postResponse.setLast(page.isLast());
        postResponse.setTotalContent(page.getTotalElements());


        return postResponse;
    }

    @Override
    public PostCommentDto getPost(long id){
        Post byId = postRepository.findById(id).orElseThrow(()-> new RuntimeException("Post Not Found"));
        PostDto postDto = postToDto(byId);

//        RestTemplate restTemplate = new RestTemplate();
        List<CommentDto> comments = getComments(postDto.getId());
        CategoryDto category = getCategory(postDto.getCategoryId());
        String categoryName = category.getCategoryName();
        PostCommentDto postCommentDto =
                PostCommentDtoMapper.postCommentDtoMapper(postDto, comments, categoryName);
        return postCommentDto;
    }

    @Override
    public void deletePost(long id){
        Post post = postRepository.findById(id).orElseThrow(()->new RuntimeException());
        List<CommentDto> comments = getComments(post.getId());
        postRepository.delete(post);

        for (CommentDto commentDto: comments){
            long commentId = commentDto.getId();
            commentClient.deleteComments(commentId);
        }
    }

    @Override
    public PostDto updatePost(PostDto postDto, long id){
        Post post = postRepository.findById(id).orElseThrow(()->new RuntimeException());
//        Category category = categoryRepository.findById(postDto.getCategoryId()).orElseThrow(
//                () -> new ResourceNotFoundException("Category", "id", postDto.getCategoryId())
//        );
        post.setTitle(postDto.getTitle());
        post.setDescription(postDto.getDescription());
        post.setContent(postDto.getContent());
//        post.setCategory(category);

        Post updatedPost = postRepository.save(post);
        return postToDto(updatedPost);
    }

    @Override
    public List<PostDto> getPostsByCategory(long categoryId) {

//        categoryRepository.findById(categoryId).orElseThrow(
//                ()-> new ResourceNotFoundException("Category", "id", categoryId)
//        );
        List<Post> byCategoryId = postRepository.findByCategoryId(categoryId);
        return byCategoryId.stream().map(post -> postToDto(post)).toList();
    }

    private Post dtoToPost(PostDto postDto){
        Post post = mapper.map(postDto, Post.class);
        return post;
    }

    private PostDto postToDto(Post post){
        PostDto postResponse = mapper.map(post, PostDto.class);
        return postResponse;
    }

    private CategoryDto getCategory(long categoryId){
//        ResponseEntity<CategoryDto> forEntity = restTemplate.getForEntity(
//                "http://CATEGORYSERVICE:8083/api/categories/"+categoryId,
//                CategoryDto.class);

        CategoryDto category = categoryClient.getCategory(categoryId);
        return category;
    }

    private List<CommentDto> getComments(long postId){
//        ResponseEntity<CommentDto[]> forEntity = restTemplate.getForEntity(
//                "http://COMMENTSERVICE:8082/api/comments?postId=" + postId,
//                CommentDto[].class);
        List<CommentDto> comments = commentClient.getComments(postId);
        return comments;
    }
}

