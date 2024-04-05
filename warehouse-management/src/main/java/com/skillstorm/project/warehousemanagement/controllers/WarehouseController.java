package com.skillstorm.project.warehousemanagement.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.skillstorm.project.warehousemanagement.exceptions.CapacityViolationException;
import com.skillstorm.project.warehousemanagement.exceptions.EmptyWarehouseException;
import com.skillstorm.project.warehousemanagement.exceptions.SameNameException;
import com.skillstorm.project.warehousemanagement.models.Warehouse;
import com.skillstorm.project.warehousemanagement.services.WarehouseManagementFacade;
import com.skillstorm.project.warehousemanagement.services.WarehouseService;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/warehouses")
@CrossOrigin("http://127.0.0.1:5500/")
public class WarehouseController {

    @Autowired
    WarehouseService warehouseService;

    @Autowired
    WarehouseManagementFacade warehouseManagementFacade;

    @GetMapping
    public ResponseEntity<List<Warehouse>> findAllWarehouses() {
        List<Warehouse> warehouse = warehouseService.findAllWarehouses();

        return new ResponseEntity<List<Warehouse>>(warehouse, HttpStatus.OK);
    }

    @GetMapping("/warehouse/{id}")
    public ResponseEntity<Warehouse> findWarehouseById(@PathVariable int id) {
        Warehouse warehouse = warehouseService.findWarehouseById(id);

        return new ResponseEntity<Warehouse>(warehouse, HttpStatus.OK);
    }

    @GetMapping("/warehouse/name/{warehouseName}")
    public ResponseEntity<Warehouse> findWarehouseByName(@PathVariable String warehouseName) {
        Warehouse warehouse = warehouseService.findWarehouseByName(warehouseName);

        return new ResponseEntity<Warehouse>(warehouse, HttpStatus.OK);
    }

    @PostMapping("/warehouse")
    public ResponseEntity<?> createWarehouse(@RequestBody Warehouse warehouse) {
        try {
            Warehouse newWarehouse = warehouseService.saveWarehouse(warehouse);
            return new ResponseEntity<Warehouse>(newWarehouse, HttpStatus.OK);  
        } catch (CapacityViolationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (SameNameException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PutMapping("/warehouse")
    public ResponseEntity<?> updateWarehouse(@RequestBody Warehouse warehouse) {
        try {
            Warehouse newWarehouse = warehouseService.updateWarehouse(warehouse);
            return new ResponseEntity<Warehouse>(newWarehouse, HttpStatus.OK);  
        } catch (CapacityViolationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @DeleteMapping("/warehouse")
    public ResponseEntity<?> deleteWarehouse(@RequestBody Warehouse warehouse) throws EmptyWarehouseException {
        try {
            warehouseManagementFacade.deleteWarehouseAndAssociatedData(warehouse.getId());
            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (EmptyWarehouseException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
        
    }

    @ExceptionHandler(CapacityViolationException.class)
    public ResponseEntity<String> handleCapacityViolationException(CapacityViolationException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }
    
}