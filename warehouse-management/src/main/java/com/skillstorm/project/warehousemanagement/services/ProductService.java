package com.skillstorm.project.warehousemanagement.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.skillstorm.project.warehousemanagement.dtos.ProductCreationDTO;
import com.skillstorm.project.warehousemanagement.exceptions.CapacityExceededException;
import com.skillstorm.project.warehousemanagement.models.Category;
import com.skillstorm.project.warehousemanagement.models.Product;
import com.skillstorm.project.warehousemanagement.repositories.ProductRepository;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

@Service
public class ProductService {

  @Autowired
  private ProductRepository productRepository;

  @Autowired
  private CategoryService categoryService;

  @Autowired
  private InventoryService inventoryService;


  public List<Product> findAllProducts() {
    return productRepository.findAll();
  }

  public Product findProductById(int id) {
    Optional<Product> product =  productRepository.findById(id);

    if (product.isPresent()) {
      return product.get();
    }

    return null;
  }

  public Product findProductByName(String name) {
    Optional<Product> product =  productRepository.findByName(name);

    if (product.isPresent()) {
      return product.get();
    }

    return null;
  }

  public List<Product> findProductsByCategory(int categoryId) {
    Optional<List<Product>> products = productRepository.findByCategory_Id(categoryId);
    
    if (products.isPresent()) {
      return products.get();
    }

    return null;
  }

  @Transactional
  public Product insertProductAndUpdateInventory(ProductCreationDTO productDetails) throws CapacityExceededException, EntityNotFoundException { 
    Product product = createOrUpdateProduct(productDetails);
    inventoryService.updateOrCreateInventory(productDetails.getWarehouseId(), product.getId(), productDetails.getQuantity(), product.getSize());
    return product;
  }

  @Transactional
  public ProductCreationDTO updateProductAndUpdateInventory(ProductCreationDTO productDetails) throws CapacityExceededException, EntityNotFoundException {
    Product product = createOrUpdateProduct(productDetails);
    inventoryService.updateInventory(productDetails.getWarehouseId(), product.getId(), productDetails.getQuantity(), productDetails.getSize());
    return productDetails;
  }

  private Product createOrUpdateProduct(ProductCreationDTO productDetails) {
      Optional<Product> optionalProduct = productRepository.findByName(productDetails.getProductName());
      Product product;

      if (optionalProduct.isPresent()) {
          product = optionalProduct.get();
          product.setDescription(productDetails.getDescription());
          product.setPrice(productDetails.getPrice());
      } else {
          Category category = categoryService.findOrCreateCategory(productDetails.getCategoryName());
          product = new Product(
                  productDetails.getProductName(),
                  productDetails.getPrice(),
                  category,
                  productDetails.getSize(),
                  productDetails.getDescription()
          );
      }
      product = productRepository.save(product);
      return product;
  }

  public Product saveProduct(Product product) {
    return productRepository.save(product);
  }

  public void deleteProduct(Product product) {
    // Delete the product from inventory
    inventoryService.deleteInventoryByProductId(product.getId(), product.getSize());

    // Then, remove product from products table
    productRepository.delete(product);

    // Finally, delete the category if it is the last one of its kind
    if (isLastProductInCategory(product.getCategory().getId())) {
      categoryService.deleteCategory(product.getCategory());
    }
  }

  @Transactional
  public void deleteProductFromWarehouse(int warehouseId, Product product) {
    boolean safeToDeleteInventory = false;
    if (inventoryService.isLastWarehouseWithProduct(product.getId())) {
      safeToDeleteInventory = true;
    }

    // First, delete inventory records of product in warehouse
    inventoryService.deleteInventoryByProductIdAndWarehouseId(product.getId(), warehouseId, product.getSize());

    // Then, delete the category if it is the last one of its kind
    if (isLastProductInCategory(product.getCategory().getId())) {
      categoryService.deleteCategory(product.getCategory());
    }

    // Finally, if there is only 1 warehouse with this product, it is the last one; delete it
    if (safeToDeleteInventory) {
      productRepository.delete(product);
    }
  }

  private boolean isLastProductInCategory(int categoryId) {
    // Count the number of products in the category
    int productCount = productRepository.countByCategory_Id(categoryId);
    return productCount == 1; // If there is only one product, it's the last one
  }
}
