package com.skillstorm.project.warehousemanagement.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.skillstorm.project.warehousemanagement.exceptions.CapacityExceededException;
import com.skillstorm.project.warehousemanagement.exceptions.CapacityViolationException;
import com.skillstorm.project.warehousemanagement.exceptions.SameNameException;
import com.skillstorm.project.warehousemanagement.models.Warehouse;
import com.skillstorm.project.warehousemanagement.models.Product.ProductSize;
import com.skillstorm.project.warehousemanagement.repositories.WarehouseRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class WarehouseService {

  @Autowired
  WarehouseRepository warehouseRepository;


  public List<Warehouse> findAllWarehouses() {
    return warehouseRepository.findAll();
  }

  public List<Integer> findAllIds() {
    return warehouseRepository.findAllIds();
  }

  public Warehouse findWarehouseById(int id) {
    // if our database has a warehouse matching id, it will return it. otherwise it will return an empty optional object
    Optional<Warehouse> warehouse =  warehouseRepository.findById(id);

    if (warehouse.isPresent()) {    // isPresent() checks if the optional returned the Warehouse object
      return warehouse.get();       // gets the Warehouse object from within the Optional object
    }

    return null;
  }

  public Warehouse findWarehouseByName(String name) {
    
    Optional<Warehouse> warehouse =  warehouseRepository.findByName(name);

    if (warehouse.isPresent()) { 
      return warehouse.get();       
    }

    return null;
  }

  public Warehouse updateWarehouse(Warehouse warehouse) throws CapacityViolationException {
    if (warehouse.getMaxCapacity() < warehouse.getCurrentCapacity()) {
      throw new CapacityViolationException("Max capacity cannot be lower than current capacity");
    }
    return warehouseRepository.save(warehouse);
  }

  public void updateWarehouseCapacity(int warehouseId, int incomingQuantity, ProductSize size) throws CapacityExceededException, EntityNotFoundException {
    Warehouse warehouse = warehouseRepository.findById(warehouseId).orElseThrow(() -> new EntityNotFoundException("Warehouse not found"));
    int newCurrentCapacity = warehouse.getCurrentCapacity() + (incomingQuantity * size.getCapacity());

    if (newCurrentCapacity > warehouse.getMaxCapacity()) {
        throw new CapacityExceededException("Adding this product exceeds the warehouse's max capacity");
    }

    warehouse.setCurrentCapacity(newCurrentCapacity);
    warehouseRepository.save(warehouse);
  }

  public void changeWarehouseCapacity(int warehouseId, int oldQuantity, int newQuantity, ProductSize size) throws CapacityExceededException {
    Warehouse warehouse = warehouseRepository.findById(warehouseId).orElseThrow(() -> new EntityNotFoundException("Warehouse not found"));
    int newCurrentCapacity = warehouse.getCurrentCapacity() + ((newQuantity - oldQuantity) * size.getCapacity());

    if (newCurrentCapacity > warehouse.getMaxCapacity()) {
      throw new CapacityExceededException("Adding this product exceeds the warehouse's max capacity");
    }

    warehouse.setCurrentCapacity(newCurrentCapacity);
    warehouseRepository.save(warehouse);
  }

  public void reduceCurrentCapacity(int warehouseId, int amount) {
    warehouseRepository.reduceCurrentCapacity(warehouseId, amount);
  }

  public Warehouse saveWarehouse(Warehouse warehouse) throws CapacityViolationException, SameNameException {
    Optional<Warehouse> toFind = warehouseRepository.findByName(warehouse.getName());
    if (toFind.isPresent()) {
      throw new SameNameException("Warehouse with that name already exists.");
    }
    if (warehouse.getMaxCapacity() < warehouse.getCurrentCapacity()) {
      throw new CapacityViolationException("Max capacity cannot be lower than current capacity");
    }
    return warehouseRepository.save(warehouse);
  }

  public void deleteWarehouse(int warehouseId) {
    warehouseRepository.deleteById(warehouseId);
  }

}
