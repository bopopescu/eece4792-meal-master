package com.app.eece4792mealmaster.controllers;

final class Routes {
  private Routes() {}

  private static final String API = "/api";

  // Path Variables
  static final String USER_ID = "/{userId}";
  static final String RECIPE_ID = "/{recipeId}";

  // User Routes
  static final String USER = "/user";
  static final String USER_API = API + USER;
  static final String LOGIN = USER_API + "/login";
  static final String LOGOUT = USER_API + "/logout";
  static final String REGISTER = USER_API + "/register";
  static final String PROFILE = USER_API + "/profile";

  // Product Routes
  static final String PRODUCT = "/product";
  static final String PRODUCT_API = API + PRODUCT;

  // Recipe Routes
  static final String RECIPE = "/recipe";
  static final String RECIPE_API = API + RECIPE;

  // Query Params
  static final String SEARCH = "/search";
  static final String SEARCHTERMS = "searchTerms";
}
