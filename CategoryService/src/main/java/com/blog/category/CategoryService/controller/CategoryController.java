package com.blog.category.CategoryService.controller;

import com.blog.category.CategoryService.payload.CategoryDto;
import com.blog.category.CategoryService.payload.CategoryPostDto;
import com.blog.category.CategoryService.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/categories")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;


    @PostMapping("")
    public ResponseEntity<CategoryDto> addCategory(@RequestBody CategoryDto categoryDto){
        CategoryDto category = categoryService.createCategory(categoryDto);
        return new ResponseEntity<>(category, HttpStatus.CREATED);
    }


    @PutMapping("{id}")
    public ResponseEntity<CategoryDto> updateCategory(
            @RequestBody CategoryDto categoryDto, @PathVariable  long id){
        return ResponseEntity.ok(categoryService.updateCategory(categoryDto, id));
    }

    @GetMapping("")
    public ResponseEntity<List<CategoryDto>> getAllCategories(){
        return ResponseEntity.ok(categoryService.getAllCategories());
    }

    @GetMapping("{id}")
    public ResponseEntity<CategoryDto> getCategory(@PathVariable long id){
        return ResponseEntity.ok(categoryService.getCategory(id));
    }


    @DeleteMapping("{id}")
    public ResponseEntity<String> deleteCategory(@PathVariable long id){
        categoryService.deleteCategory(id);

        return ResponseEntity.ok("Category deleted successfully");
    }

}
