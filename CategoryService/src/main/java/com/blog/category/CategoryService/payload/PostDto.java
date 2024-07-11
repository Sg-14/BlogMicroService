package com.blog.category.CategoryService.payload;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PostDto {
    private long id;
    private String title;
    private String description;
    private String content;
}
