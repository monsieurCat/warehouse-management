package com.skillstorm.project.warehousemanagement.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;

/*
CREATE TABLE inventory (
  product_id INT NOT NULL REFERENCES products(id),
  warehouse_id INT NOT NULL REFERENCES warehouses(id),
  quantity INT NOT NULL,
  PRIMARY KEY (warehouse_id, product_id)
);
 */

@Entity
@Table(name = "inventory")
@IdClass(InventoryId.class)
public class Inventory {

  @Id
  private int productId;

  @Id
  private int warehouseId;

  @Column
  private int quantity;

  public Inventory() { } 

  public int getProductId() {
    return productId;
  }

  public void setProductId(int productId) {
    this.productId = productId;
  }

  public int getWarehouseId() {
    return warehouseId;
  }

  public void setWarehouseId(int warehouseId) {
    this.warehouseId = warehouseId;
  }

  public Inventory(int productId, int warehouseId, int quantity) {
    this.productId = productId;
    this.warehouseId = warehouseId;
    this.quantity = quantity;
  }

  public int getQuantity() {
    return quantity;
  }

  public void setQuantity(int quantity) {
    this.quantity = quantity;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + productId;
    result = prime * result + warehouseId;
    result = prime * result + quantity;
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    Inventory other = (Inventory) obj;
    if (productId != other.productId)
      return false;
    if (warehouseId != other.warehouseId)
      return false;
    if (quantity != other.quantity)
      return false;
    return true;
  }
}