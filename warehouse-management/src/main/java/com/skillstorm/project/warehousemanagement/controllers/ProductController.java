package com.skillstorm.project.warehousemanagement.controllers;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.skillstorm.project.warehousemanagement.dtos.ProductCreationDTO;
import com.skillstorm.project.warehousemanagement.exceptions.CapacityExceededException;
import com.skillstorm.project.warehousemanagement.exceptions.EmptyWarehouseException;
import com.skillstorm.project.warehousemanagement.models.Product;
import com.skillstorm.project.warehousemanagement.services.ProductService;
import com.skillstorm.project.warehousemanagement.services.WarehouseManagementFacade;

@RestController
@RequestMapping("/products")
@CrossOrigin("http://127.0.0.1:5500/")
public class ProductController {

  @Autowired
  ProductService productService;

  @Autowired
  WarehouseManagementFacade warehouseManagementFacade;

  @GetMapping
  public ResponseEntity<List<Product>> findAllProducts() {
    List<Product> products = productService.findAllProducts();
    return new ResponseEntity<List<Product>>(products, HttpStatus.OK);
  }

  @GetMapping("/warehouse/{warehouseId}")
  public ResponseEntity<?> findProductsByWarehouseId(@PathVariable int warehouseId) {
    try {
      List<ProductCreationDTO> products = warehouseManagementFacade.findProductsByWarehouseId(warehouseId);
      return new ResponseEntity<List<ProductCreationDTO>>(products, HttpStatus.OK);
    } catch (EmptyWarehouseException e) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Collections.singletonMap("message", e.getMessage()));
    }
  }

  @GetMapping("/warehouse/name/{warehouseName}")
  public ResponseEntity<?> findProductsByWarehouseName(@PathVariable String warehouseName) {
    try {
      List<ProductCreationDTO> products = warehouseManagementFacade.findProductsByWarehouseName(warehouseName);
      return new ResponseEntity<List<ProductCreationDTO>>(products, HttpStatus.OK);
    } catch (EmptyWarehouseException e) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Collections.singletonMap("message", e.getMessage()));
    }
  }

  @GetMapping("/category/{id}")
  public ResponseEntity<List<Product>> findProductsByCategory(@PathVariable int id) {
    List<Product> products = productService.findProductsByCategory(id);

    return new ResponseEntity<List<Product>>(products, HttpStatus.OK);
  }

  @GetMapping("/product/{id}")
  public ResponseEntity<Product> findProductById(@PathVariable int id) {
      Product product = productService.findProductById(id);

      return new ResponseEntity<Product>(product, HttpStatus.OK);
  }

  @GetMapping("/product/name/{name}")
  public ResponseEntity<Product> findProductByName(@PathVariable String name) {
    Product product = productService.findProductByName(name);

    return new ResponseEntity<Product>(product, HttpStatus.OK);
  }

  /* ProductCreationDTO(String productName, 
                        BigDecimal price, 
                        ProductSize size, 
                        String description, 
                        int warehouseId,
                        int quantity, 
                        String categoryName) */
  @PostMapping("/product")
  public ResponseEntity<?> addProductAndInventory(@RequestBody ProductCreationDTO productDetails) {
    try {
      Product product = productService.insertProductAndUpdateInventory(productDetails);
      return new ResponseEntity<Product>(product, HttpStatus.OK);
    } catch (CapacityExceededException e) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Collections.singletonMap("message", e.getMessage()));
    }
  }

  @PutMapping("/product")
  public ResponseEntity<?> updateProductAndInventory(@RequestBody ProductCreationDTO productDetails) {
    try {
      ProductCreationDTO product = productService.updateProductAndUpdateInventory(productDetails);
      return new ResponseEntity<ProductCreationDTO>(product, HttpStatus.OK);
    } catch (CapacityExceededException e) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Collections.singletonMap("message", e.getMessage()));
    }
  }

  @DeleteMapping("/product")
  public ResponseEntity<Product> deleteProduct(@RequestBody Product product) {
      productService.deleteProduct(product);
      return ResponseEntity.noContent().build();
  }

  @DeleteMapping("/product/warehouse/{warehouseId}")
  public ResponseEntity<?> deleteProductFromWarehouse(@PathVariable int warehouseId, @RequestBody Product product) {
      productService.deleteProductFromWarehouse(warehouseId, product);
      return ResponseEntity.noContent().build();
  }
  
}
