package com.skillstorm.project.warehousemanagement.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.skillstorm.project.warehousemanagement.models.Warehouse;

import jakarta.transaction.Transactional;

/*
 * Repositories talk to our database
 *    - no business logic (ex. if statements to do anything)
 *    - needs to be an interface that extends an existing repository class
 *        - JpaRepository is one of those classes
 *        - other types:
 *            CrudRepository                - crud operations
 *            PagingAndSortingRepository    - extends the CrudRepository and includes methods for pagination and sorting
 *            JpaRepository                 - extends PagingAndSortingRepository but adds a lot of extra functionality (methods)
 */

@Repository
public interface WarehouseRepository extends JpaRepository<Warehouse, Integer> {

  public Optional<Warehouse> findByName(String name);

  @Query("SELECT w.id FROM Warehouse w")
  public List<Integer> findAllIds();
  
  @Transactional
  @Modifying
  @Query("UPDATE Warehouse w SET w.currentCapacity = w.currentCapacity - :amount WHERE w.id = :warehouseId")
  void reduceCurrentCapacity(@Param("warehouseId") int warehouseId, @Param("amount") int amount);

}
