package com.skillstorm.project.warehousemanagement.services;

import org.springframework.stereotype.Service;

import com.skillstorm.project.warehousemanagement.dtos.ProductCreationDTO;
import com.skillstorm.project.warehousemanagement.exceptions.EmptyWarehouseException;
import com.skillstorm.project.warehousemanagement.models.Inventory;
import com.skillstorm.project.warehousemanagement.models.Product;
import com.skillstorm.project.warehousemanagement.models.Warehouse;

import java.util.ArrayList;
import java.util.List;


@Service
public class WarehouseManagementFacade {
  private final WarehouseService warehouseService;
  private final ProductService productService;
  private final InventoryService inventoryService;

  public WarehouseManagementFacade(WarehouseService warehouseService, ProductService productService, InventoryService inventoryService) {
      this.warehouseService = warehouseService;
      this.productService = productService;
      this.inventoryService = inventoryService;
  }

  // Delete warehouse, products, and inventory associated with the warehouse
  public void deleteWarehouseAndAssociatedData(int warehouseId) throws EmptyWarehouseException {

    // Find all records of products in this warehouse
    List<Inventory> warehouseInventory = inventoryService.findByWarehouseId(warehouseId);

    // For each record
    for (Inventory row : warehouseInventory) {

      // If it is the last warehouse with this product
      if (inventoryService.isLastWarehouseWithProduct(row.getProductId())) {

        // Find the actual product
        Product product = productService.findProductById(row.getProductId());
        if (product != null) {

          // Remove product from products and inventory table and handle category check
          productService.deleteProduct(product);
        }
      }
    }
      
    // Now we can delete the warehouse safely
    warehouseService.deleteWarehouse(warehouseId);
  }

  // Find all products by warehouse
  public List<ProductCreationDTO> findProductsByWarehouseId(int warehouseId) throws EmptyWarehouseException {

    // Find inventory records for this warehouse
    List<Inventory> inventory = inventoryService.findByWarehouseId(warehouseId);

    // Initialize our products array
    List<ProductCreationDTO> products = new ArrayList<>();

    // If there are records
    if (inventory.size() > 0) {

      // For each record
      for (Inventory inv : inventory) {

        // Get the product
        Product product = productService.findProductById(inv.getProductId());
        if (product != null) {

          // Create representation to display to user
          ProductCreationDTO fullProduct = new ProductCreationDTO(product.getName(), 
                                                                  product.getPrice().toString(), 
                                                                  product.getSize().toString(), 
                                                                  product.getDescription(), 
                                                                  warehouseId, 
                                                                  inv.getQuantity(), 
                                                                  product.getCategory().getName());
          // Add to products array
          products.add(fullProduct);                                                                  
        }
      }
      return products;
    }
    return products;
  }
  
  public List<ProductCreationDTO> findProductsByWarehouseName(String warehouseName) throws EmptyWarehouseException {
    Warehouse warehouse = warehouseService.findWarehouseByName(warehouseName);

    return findProductsByWarehouseId(warehouse.getId());
  }
}