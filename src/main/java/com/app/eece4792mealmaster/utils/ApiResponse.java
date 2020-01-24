package com.app.eece4792mealmaster.utils;

import org.springframework.http.HttpStatus;

public class ApiResponse {

  private int status;
  private String message;
  private Object result;

  public ApiResponse(int status, String message, Object result) {
    this.status = status;
    this.message = message;
    this.result = result;
  }

  public ApiResponse(String message) {
    this(200, message, null);
  }

  public ApiResponse(Object result) {
    this(200, "Success", result);
  }

  public ApiResponse() {
    this(200, "Success", null);
  }

  public int getStatus() {
    return status;
  }

}