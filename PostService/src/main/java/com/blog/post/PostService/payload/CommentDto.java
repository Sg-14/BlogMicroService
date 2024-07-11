package com.blog.post.PostService.payload;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CommentDto {
    private long id;

    @NotEmpty(message = "The name of the person can't be empty")
    private String name;
    @Email(message = "Enter a valid email address")
    private String email;
    @NotEmpty(message = "Empty comment is not allowed")
    private String body;

    private long postId;
}
