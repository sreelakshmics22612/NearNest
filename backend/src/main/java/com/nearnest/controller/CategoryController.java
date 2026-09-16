package com.nearnest.controller;

import com.nearnest.model.Category;
import com.nearnest.repository.CategoryRepository;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
@CrossOrigin(origins = "http://localhost:4200")
public class CategoryController {

    private final CategoryRepository categoryRepository;

    public CategoryController(
            CategoryRepository categoryRepository) {

        this.categoryRepository = categoryRepository;
    }


    // GET ALL CATEGORIES
    @GetMapping
    public List<Category> getCategories() {

        return categoryRepository.findAll();
    }
}