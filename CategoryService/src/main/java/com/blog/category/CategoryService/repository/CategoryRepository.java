package com.blog.category.CategoryService.repository;

import com.blog.category.CategoryService.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {

}
