package com.skillstorm.project.warehousemanagement.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.skillstorm.project.warehousemanagement.exceptions.CapacityExceededException;
import com.skillstorm.project.warehousemanagement.exceptions.EmptyWarehouseException;
import com.skillstorm.project.warehousemanagement.models.Inventory;
import com.skillstorm.project.warehousemanagement.models.InventoryId;
import com.skillstorm.project.warehousemanagement.models.Warehouse;
import com.skillstorm.project.warehousemanagement.models.Product.ProductSize;
import com.skillstorm.project.warehousemanagement.repositories.InventoryRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class InventoryService {

  @Autowired
  InventoryRepository inventoryRepository;

  @Autowired
  WarehouseService warehouseService;

  /* @Autowired
  WarehouseRepository warehouseRepository; */

  public List<Inventory> findByProductId(int productId) {
    Optional<List<Inventory>> inventory = inventoryRepository.findByProductId(productId);
    if (inventory.isPresent()) {
      return inventory.get();
    }
    return List.of();
  }

  public List<Inventory> findByWarehouseId(int warehouseId) throws EmptyWarehouseException{
    Optional<List<Inventory>> inventory = inventoryRepository.findByWarehouseId(warehouseId);
    if (!inventory.isPresent()) {
      throw new EmptyWarehouseException("This warehouse is empty.");
    }
    return inventory.get();
  }

  public List<Inventory> findByWarehouseName(String warehouseName) throws EmptyWarehouseException {
    Warehouse warehouse = warehouseService.findWarehouseByName(warehouseName);
    return findByWarehouseId(warehouse.getId());
  }

  public Inventory updateOrCreateInventory(int warehouseId, int productId, int quantity, ProductSize size) throws CapacityExceededException, EntityNotFoundException {
    warehouseService.updateWarehouseCapacity(warehouseId, quantity, size); 

    Optional<Inventory> optionalInventory = inventoryRepository.findByWarehouseIdAndProductId(warehouseId, productId);
    Inventory inventory;

    if (optionalInventory.isPresent()) {
        inventory = optionalInventory.get();
        inventory.setQuantity(inventory.getQuantity() + quantity);
    } else {
        inventory = new Inventory(
                productId,
                warehouseId,
                quantity
        );
    }
    return inventoryRepository.save(inventory);
  }

  // Updates the inventory record of the specified product in the specified warehouse
  public void updateInventory(int warehouseId, int productId, int quantity, ProductSize size) throws CapacityExceededException {

    // First, get the existing inventory record for this product and warehouse
    Optional<Inventory> optionalInventory = inventoryRepository.findByWarehouseIdAndProductId(warehouseId, productId);
    Inventory inventory = optionalInventory.get();

    // Keep track of how many there are before the update.
    // Need to pass this to the warehouse so it can update its capacity
    int oldQuantity = inventory.getQuantity();

    // Try to update the warehouse's capacity
    // Throws CapacityExceededException if new capacity exceeds its max capacity
    warehouseService.changeWarehouseCapacity(warehouseId, oldQuantity, quantity, size);

    // If it succeeds, then we can safely update the inventory record now
    inventory.setQuantity(quantity);
    inventoryRepository.save(inventory);
  }

  public void deleteInventoryByProductId(int productId, ProductSize size) {
    // Get ids of all warehouses
    List<Integer> warehouseIds = warehouseService.findAllIds();

    // For each warehouse
    for (int warehouseId : warehouseIds) {
      // Delete the inventory record for product and update warehouse capacity
      deleteInventoryByProductIdAndWarehouseId(productId, warehouseId, size);
    }
  }

  public void deleteInventoryByProductIdAndWarehouseId(int productId, int warehouseId, ProductSize size) {
    // Calculate the capacity being removed
    Optional<Integer> quantity = inventoryRepository.findQuantityByProductIdAndWarehouseId(productId, warehouseId);
    if (!quantity.isPresent()) return;
    int amount = size.getCapacity() * quantity.get();

    // Delete inventory record for product in this warehouse
    InventoryId inventoryId = new InventoryId(productId, warehouseId);
    inventoryRepository.deleteById(inventoryId);

    // Subtract the capacity removed from this warehouse
    warehouseService.reduceCurrentCapacity(warehouseId, amount);
  }

  public boolean isLastWarehouseWithProduct(int productId) {
    // Get the number of warehouses with this product
    int warehouseCount = inventoryRepository.countByProductId(productId);

    // If there is only 1 warehouse with this product, it is the last one
    return warehouseCount == 1;
  }
}
