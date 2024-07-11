package com.blog.post.PostService.payload;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NonNull;

import java.util.Set;


@Data
public class PostDto {
    private long id;

    @NotEmpty
    @Size(min = 2, message = "The title should have atleast 2 characters")
    private String title;
    @NotEmpty @Size(min = 5, message = "The description should have atleast 5 characters")
    private String description;
    @NotEmpty
    private String content;

    private Long categoryId;
}
