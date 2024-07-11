package com.blog.category.CategoryService.service.impl;

import com.blog.category.CategoryService.entity.Category;
import com.blog.category.CategoryService.payload.CategoryDto;
import com.blog.category.CategoryService.payload.CategoryPostDto;
import com.blog.category.CategoryService.payload.PostDto;
import com.blog.category.CategoryService.repository.CategoryRepository;
import com.blog.category.CategoryService.service.CategoryService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImplementation implements CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ModelMapper mapper;


    @Override
    public List<CategoryDto> getAllCategories() {
        List<Category> categories = categoryRepository.findAll();
        List<CategoryDto> collect = categories.stream().map(category -> categoryToDto(category)).collect(Collectors.toList());
        return collect;
    }

    @Override
    public CategoryDto createCategory(CategoryDto categoryDto) {
        Category category = dtoToCategory(categoryDto);
        Category newCategory = categoryRepository.save(category);

        return categoryToDto(newCategory);
    }

    @Override
    public CategoryDto getCategory(long id) {
        Category category = categoryRepository.findById(id).orElseThrow(
                ()-> new RuntimeException()
        );

        return categoryToDto(category);
    }

    @Override
    public CategoryDto updateCategory(CategoryDto categoryDto, long id) {
        Category category = categoryRepository.findById(id).orElseThrow(
                () -> new RuntimeException()
        );

        category.setCategoryName(categoryDto.getCategoryName());

        Category save = categoryRepository.save(category);

        return  categoryToDto(save);
    }

    @Override
    public void deleteCategory(long id) {
        Category category = categoryRepository.findById(id).orElseThrow(
                () -> new RuntimeException()
        );

        categoryRepository.delete(category);
    }


    private Category dtoToCategory(CategoryDto categoryDto){
        return mapper.map(categoryDto, Category.class);
    }

    private CategoryDto categoryToDto(Category category){
        return mapper.map(category, CategoryDto.class);
    }
}
