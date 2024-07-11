package com.blog.comment.CommentService.repostiory;

import com.blog.comment.CommentService.entity.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByPostId(long id);
    Comment findByPostIdAndId(@Param("post_id") long postId, @Param("id") long commentId);
}
