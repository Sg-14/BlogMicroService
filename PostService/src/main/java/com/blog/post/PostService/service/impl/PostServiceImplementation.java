package com.blog.post.PostService.service.impl;

import com.blog.post.PostService.clients.CategoryClient;
import com.blog.post.PostService.clients.CommentClient;
import com.blog.post.PostService.entity.Post;
import com.blog.post.PostService.payload.*;
import com.blog.post.PostService.repository.PostRepository;
import com.blog.post.PostService.service.PostService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

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

    public static int attempts = 0;

    @Override
    @CircuitBreaker(name = "commentBreaker", fallbackMethod = "getAllPostsFallBack")
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
    public PostResponse getAllPostsFallBack(int pageNo, int pageSize, String sortBy, String sortDir, Throwable throwable){
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name())
                ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
        Page<Post> pages = postRepository.findAll(pageable);

        List<Post> posts = pages.getContent();
        List<PostDto> postDtos = posts.stream().map(post -> postToDto(post)).toList();
        List<PostCommentDto> postCommentDtos = new ArrayList<>();

        postDtos.forEach(
                postDto -> {
                    PostCommentDto postCommentDto = PostCommentDtoMapper.postCommentDtoMapper(
                            postDto, Collections.emptyList(), "");
                    postCommentDtos.add(postCommentDto);
                }
        );

        PostResponse postResponse = new PostResponse();

        postResponse.setContent(postCommentDtos);
        postResponse.setPageNo(pages.getNumber());
        postResponse.setPageSize(pages.getSize());
        postResponse.setTotalPages(pages.getTotalPages());
        postResponse.setLast(pages.isLast());
        postResponse.setTotalContent(pages.getTotalElements());


        return postResponse;
    }

    @Override
    @Retry(name = "commentBreaker", fallbackMethod = "getPostFallBack")
    public PostCommentDto getPost(long id, Map<String, String> header){
        System.out.println(attempts);
        attempts++;

        String username = header.get("username");

        System.out.println(username);

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

    public PostCommentDto getPostFallBack(long id, Map<String, String> header, Throwable throwable){
        Post post = postRepository.findById(id).orElseThrow();
        PostDto postDto = postToDto(post);
        PostCommentDto postCommentDto = PostCommentDtoMapper.
                postCommentDtoMapper(postDto, Collections.emptyList(), "");
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
