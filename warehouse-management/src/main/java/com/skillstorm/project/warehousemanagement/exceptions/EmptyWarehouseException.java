package com.skillstorm.project.warehousemanagement.exceptions;

public class EmptyWarehouseException extends Exception {
  public EmptyWarehouseException(String errMsg) {
    super(errMsg);
  }
}
