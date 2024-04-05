package com.skillstorm.project.warehousemanagement.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.skillstorm.project.warehousemanagement.models.Inventory;
import com.skillstorm.project.warehousemanagement.models.InventoryId;
import java.util.List;


@Repository
public interface InventoryRepository extends JpaRepository<Inventory, InventoryId> {

  Optional<Inventory> findByWarehouseIdAndProductId(int warehouseId, int productId);

  Optional<List<Inventory>> findByWarehouseId(int warehouseId);

  Optional<List<Inventory>> findByProductId(int productId);

  void deleteByProductId(int productId);

  int countByProductId(int productId);

  @Query("SELECT COALESCE(i.quantity, 0) FROM Inventory i WHERE i.productId = :productId AND i.warehouseId = :warehouseId")
  Optional<Integer> findQuantityByProductIdAndWarehouseId(@Param("productId") int productId, @Param("warehouseId") int warehouseId);

  void deleteById(InventoryId id);

  void deleteByWarehouseId(int warehouseId);
}