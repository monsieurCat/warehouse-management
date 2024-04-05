package com.skillstorm.project.warehousemanagement.dtos;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.skillstorm.project.warehousemanagement.models.Product.ProductSize;

public class ProductCreationDTO {

  private int id;
  private String productName;
  private BigDecimal price;
  private ProductSize size;
  private String description;
  private int warehouseId;
  private int quantity;
  private String categoryName;

  public ProductCreationDTO() { }

  public ProductCreationDTO(int id, String productName, BigDecimal price, ProductSize size, String description, int warehouseId,
    int quantity, String categoryName) {
    this.id = id;
    this.productName = productName;
    this.price = price;
    this.size = size;
    this.description = description;
    this.warehouseId = warehouseId;
    this.quantity = quantity;
    this.categoryName = categoryName;
  }
  
  @JsonCreator
  public ProductCreationDTO(@JsonProperty("productName") String productName, 
                            @JsonProperty("price") String price, 
                            @JsonProperty("size") String size, 
                            @JsonProperty("description") String description, 
                            @JsonProperty("warehouseId") int warehouseId,
                            @JsonProperty("quantity") int quantity, 
                            @JsonProperty("categoryName") String categoryName) {
    this.productName = productName;
    this.price = new BigDecimal(price);
    this.size = ProductSize.valueOf(size.toUpperCase());
    this.description = description;
    this.warehouseId = warehouseId;
    this.quantity = quantity;
    this.categoryName = categoryName;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getProductName() {
    return productName;
  }

  public void setProductName(String productName) {
    this.productName = productName;
  }

  public BigDecimal getPrice() {
    return price;
  }

  public void setPrice(BigDecimal price) {
    this.price = price;
  }

  public ProductSize getSize() {
    return size;
  }

  public void setSize(ProductSize size) {
    this.size = size;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public int getWarehouseId() {
    return warehouseId;
  }

  public void setWarehouseId(int warehouseId) {
    this.warehouseId = warehouseId;
  }

  public int getQuantity() {
    return quantity;
  }

  public void setQuantity(int quantity) {
    this.quantity = quantity;
  }

  public String getCategoryName() {
    return categoryName;
  }

  public void setCategoryName(String categoryName) {
    this.categoryName = categoryName;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((productName == null) ? 0 : productName.hashCode());
    result = prime * result + ((price == null) ? 0 : price.hashCode());
    result = prime * result + ((size == null) ? 0 : size.hashCode());
    result = prime * result + ((description == null) ? 0 : description.hashCode());
    result = prime * result + warehouseId;
    result = prime * result + quantity;
    result = prime * result + ((categoryName == null) ? 0 : categoryName.hashCode());
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
    ProductCreationDTO other = (ProductCreationDTO) obj;
    if (productName == null) {
      if (other.productName != null)
        return false;
    } else if (!productName.equals(other.productName))
      return false;
    if (price == null) {
      if (other.price != null)
        return false;
    } else if (!price.equals(other.price))
      return false;
    if (size != other.size)
      return false;
    if (description == null) {
      if (other.description != null)
        return false;
    } else if (!description.equals(other.description))
      return false;
    if (warehouseId != other.warehouseId)
      return false;
    if (quantity != other.quantity)
      return false;
    if (categoryName == null) {
      if (other.categoryName != null)
        return false;
    } else if (!categoryName.equals(other.categoryName))
      return false;
    return true;
  }

  @Override
  public String toString() {
    return "ProductCreationDTO [productName=" + productName + ", price=" + price + ", size=" + size + ", description="
        + description + ", warehouseId=" + warehouseId + ", quantity=" + quantity + ", categoryName=" + categoryName
        + "]";
  }
}