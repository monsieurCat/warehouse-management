package com.skillstorm.project.warehousemanagement.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.skillstorm.project.warehousemanagement.models.Product;
import java.util.List;
import java.util.Optional;


@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {


  public Optional<List<Product>> findByCategory_Id(int categoryId);

  public int countByCategory_Id(int categoryId);

  public Optional<Product> findByName(String name);
}
