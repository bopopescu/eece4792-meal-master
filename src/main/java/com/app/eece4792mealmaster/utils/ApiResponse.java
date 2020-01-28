package com.app.eece4792mealmaster.utils;

public class ApiResponse {

  private int status;
  private String message;
  private Object body;

  public ApiResponse(int status, String message, Object body) {
    this.status = status;
    this.message = message;
    this.body = body;
  }

  public ApiResponse(String message) {
    this(200, message, null);
  }

  public ApiResponse(Object body) {
    this(200, "Success", body);
  }

  public ApiResponse(String message, Object body) {
    this(200, message, body);
  }

  public ApiResponse() {
    this(200, "Success", null);
  }

  public int getStatus() {
    return status;
  }

  public String getMessage() { return message; }

  public Object getbody() { return body; }
}