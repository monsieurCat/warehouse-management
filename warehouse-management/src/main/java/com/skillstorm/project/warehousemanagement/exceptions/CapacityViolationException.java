package com.skillstorm.project.warehousemanagement.exceptions;

public class CapacityViolationException extends Exception {
  public CapacityViolationException(String errMsg) {
    super(errMsg);
  }
}
