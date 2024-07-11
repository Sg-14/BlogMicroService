package com.blog.category.CategoryService.service;

import com.blog.category.CategoryService.payload.CategoryDto;
import com.blog.category.CategoryService.payload.CategoryPostDto;

import java.util.List;

public interface CategoryService {
    List<CategoryDto> getAllCategories();
    CategoryDto createCategory(CategoryDto categoryDto);
    CategoryDto getCategory(long id);
    CategoryDto updateCategory(CategoryDto categoryDto, long id);
    void deleteCategory(long id);
}
