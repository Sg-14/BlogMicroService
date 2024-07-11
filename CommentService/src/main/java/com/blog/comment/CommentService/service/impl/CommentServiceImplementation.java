package com.blog.comment.CommentService.service.impl;

import com.blog.comment.CommentService.entity.Comment;
import com.blog.comment.CommentService.payload.CommentDto;
import com.blog.comment.CommentService.repostiory.CommentRepository;
import com.blog.comment.CommentService.service.CommentService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class CommentServiceImplementation implements CommentService {
    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private ModelMapper mapper;
    @Override
    public CommentDto createComment(long postId, CommentDto commentDto) {
        commentDto.setPostId(postId);
        Comment comment = dtoToComment(commentDto);
//        Post post = postRepository.findById(postId).orElseThrow(
//                ()->new ResourceNotFoundException("Post", "id", postId));
//        comment.setPost(post);

        Comment commentResponse = commentRepository.save(comment);

        return commentToDto(commentResponse);
    }

    @Override
    public List<CommentDto> getAllComments(long postId) {

        List<Comment> page = commentRepository.findByPostId(postId);
        List<CommentDto> collection = page.stream().map(
                comment -> commentToDto(comment)).collect(Collectors.toList());

        return collection;
    }

    @Override
    public CommentDto getComment (long postId, long commentId) {
        Comment comment = findCommentWithPostAndCommentId(postId, commentId);
        return commentToDto(comment);
    }

    @Override
    public void deleteComment(long commentId){
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(()-> new RuntimeException());
        commentRepository.delete(comment);
    }

    @Override
    public CommentDto updateComment(long commentId, CommentDto commentDto){
        Comment comment = commentRepository.findById(commentId).orElseThrow();
        comment.setBody(Objects.equals(commentDto.getBody(), null) ? comment.getBody() : commentDto.getBody());
        comment.setName(Objects.equals(commentDto.getName(), null) ? comment.getName() : commentDto.getName());
        comment.setEmail(Objects.equals(commentDto.getEmail(), null) ? comment.getEmail() : commentDto.getEmail());

        Comment newComment = commentRepository.save(comment);

        return commentToDto(newComment);
    }

    private Comment dtoToComment(CommentDto commentDto){
        return mapper.map(commentDto, Comment.class);
    }

    private CommentDto commentToDto(Comment comment){
        return mapper.map(comment, CommentDto.class);
    }

    private Comment findCommentWithPostAndCommentId(long postId, long commentId){
        Comment comment = commentRepository.findByPostIdAndId(postId, commentId);
        if(comment == null){
            throw new RuntimeException();
        }
        return comment;
    }
}
