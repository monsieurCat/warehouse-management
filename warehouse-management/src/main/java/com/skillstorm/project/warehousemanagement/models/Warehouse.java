package com.skillstorm.project.warehousemanagement.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
CREATE TABLE warehouses (
  id SERIAL PRIMARY KEY,
  name VARCHAR(50),
  description TEXT
);
*/

@Entity                       // tells jpa that this class relates to a db table
@Table(name = "warehouses")    // tells jpa WHICH table; if class name and table name is the same, wouldn't need to specify name
public class Warehouse {

  @Id                                                      // specifies primary key
  @Column                                                  // tells db that this is a column
  @GeneratedValue(strategy = GenerationType.IDENTITY)      // tells db that it uses AUTO_INCREMENT
  private int id;

  @Column
  private String name;

  @Column
  private String description;

  @Column(name = "current_capacity")
  private int currentCapacity;

  @Column(name = "max_capacity")
  private int maxCapacity;

  public Warehouse() { }

  public Warehouse(int id, String name, String description, int maxCapacity) {
    this.id = id;
    this.name = name;
    this.description = description;
    this.maxCapacity = maxCapacity;
  }

  public Warehouse(String name, String description, int maxCapacity) {
    this.name = name;
    this.description = description;
    this.maxCapacity = maxCapacity;
  }

  public Warehouse(String name, String description) {
    this.name = name;
    this.description = description;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String firstName) {
    this.name = firstName;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public int getCurrentCapacity() {
    return currentCapacity;
  }

  public void setCurrentCapacity(int currentCapacity) {
    this.currentCapacity = currentCapacity;
  }

  public int getMaxCapacity() {
    return maxCapacity;
  }

  public void setMaxCapacity(int maxCapacity) {
    this.maxCapacity = maxCapacity;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + id;
    result = prime * result + ((name == null) ? 0 : name.hashCode());
    result = prime * result + ((description == null) ? 0 : description.hashCode());
    result = prime * result + currentCapacity;
    result = prime * result + maxCapacity;
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
    Warehouse other = (Warehouse) obj;
    if (id != other.id)
      return false;
    if (name == null) {
      if (other.name != null)
        return false;
    } else if (!name.equals(other.name))
      return false;
    if (description == null) {
      if (other.description != null)
        return false;
    } else if (!description.equals(other.description))
      return false;
    if (currentCapacity != other.currentCapacity)
      return false;
    if (maxCapacity != other.maxCapacity)
      return false;
    return true;
  }

  @Override
  public String toString() {
    return "Warehouse [id=" + id + ", name=" + name + ", description=" + description + ", currentCapacity="
        + currentCapacity + ", maxCapacity=" + maxCapacity + "]";
  }

  
}
