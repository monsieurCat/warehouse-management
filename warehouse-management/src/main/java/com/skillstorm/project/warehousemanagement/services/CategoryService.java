package com.skillstorm.project.warehousemanagement.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.skillstorm.project.warehousemanagement.models.Category;
import com.skillstorm.project.warehousemanagement.repositories.CategoryRepository;

@Service
public class CategoryService {

  @Autowired
  CategoryRepository categoryRepository;

  public List<Category> findAllCategories() {
    return categoryRepository.findAll();
  }

  public Category findCategoryById(int id) {
    Optional<Category> category =  categoryRepository.findById(id);

    if (category.isPresent()) {
      return category.get();
    }

    return null;
  }

  public Category findOrCreateCategory(String categoryName) {
    Optional<Category> optionalExistingCategory = categoryRepository.findByName(categoryName);
    if (optionalExistingCategory.isPresent()) {
        return optionalExistingCategory.get();
    } else {
        Category category = new Category(categoryName);
        return categoryRepository.save(category);
    }
  }

  public void deleteCategory(Category category) {
    categoryRepository.deleteById(category.getId());
  }
}
