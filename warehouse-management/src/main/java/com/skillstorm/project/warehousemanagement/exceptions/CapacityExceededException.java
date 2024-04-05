package com.skillstorm.project.warehousemanagement.exceptions;

public class CapacityExceededException extends Exception {
  public CapacityExceededException(String errMsg) {
    super(errMsg);
  }
}
