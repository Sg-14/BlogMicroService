package com.blog.category.CategoryService.payload;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CategoryPostDto {
    private long id;
    private String categoryName;

    private List<PostDto> postDtos;
}
