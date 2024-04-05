package com.skillstorm.project.warehousemanagement.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.skillstorm.project.warehousemanagement.models.Category;
import com.skillstorm.project.warehousemanagement.services.CategoryService;

@RestController
@RequestMapping("/categories")
@CrossOrigin("http://127.0.0.1:5500/")
public class CategoryController {

  @Autowired
  CategoryService categoryService;

  @GetMapping
  public ResponseEntity<List<Category>> findAllCategories() {
    List<Category> categories = categoryService.findAllCategories();
    return new ResponseEntity<List<Category>>(categories, HttpStatus.OK);
  }

  @GetMapping("/category/{id}")
    public ResponseEntity<Category> findMovieById(@PathVariable int id) {
        Category category = categoryService.findCategoryById(id);

        return new ResponseEntity<Category>(category, HttpStatus.OK);
    }
}