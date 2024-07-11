package com.blog.comment.CommentService.service;

import com.blog.comment.CommentService.payload.CommentDto;

import java.util.List;

public interface CommentService {
    public CommentDto createComment(long postId, CommentDto commentDto);
    public List<CommentDto> getAllComments(long postId);
    public CommentDto getComment(long postId, long commentId);
    public void deleteComment(long commentId);
    public CommentDto updateComment(long commentId, CommentDto commentDto);
}
