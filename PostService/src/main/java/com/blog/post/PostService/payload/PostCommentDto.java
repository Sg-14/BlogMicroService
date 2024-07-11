package com.blog.post.PostService.payload;

import com.blog.post.PostService.entity.Post;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class PostCommentDto {
    private long id;
    private String title;
    private String description;
    private String content;
    private String categoryName;

    private List<CommentDto> comments;
}
