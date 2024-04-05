package com.skillstorm.project.warehousemanagement.models;

import java.io.Serializable;

public class InventoryId implements Serializable {

    private int warehouseId;
    private int productId;

    public InventoryId() { }

    public InventoryId(int productId, int warehouseId) {
      this.warehouseId = warehouseId;
      this.productId = productId;
    }
    // Getters and setters
    public int getWarehouseId() {
      return warehouseId;
    }
    public void setWarehouseId(int warehouseId) {
      this.warehouseId = warehouseId;
    }
    public int getProductId() {
      return productId;
    }
    public void setProductId(int productId) {
      this.productId = productId;
    }
    @Override
    public int hashCode() {
      final int prime = 31;
      int result = 1;
      result = prime * result + warehouseId;
      result = prime * result + productId;
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
      InventoryId other = (InventoryId) obj;
      if (warehouseId != other.warehouseId)
        return false;
      if (productId != other.productId)
        return false;
      return true;
    }
    @Override
    public String toString() {
      return "InventoryId [warehouseId=" + warehouseId + ", productId=" + productId + "]";
    }

    
}
