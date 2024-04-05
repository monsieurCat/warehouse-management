package com.skillstorm.project.warehousemanagement.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.skillstorm.project.warehousemanagement.models.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer> {
  Optional<Category> findByName(String name);
}
