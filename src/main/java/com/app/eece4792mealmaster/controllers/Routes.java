package com.app.eece4792mealmaster.controllers;

final class Routes {
  private Routes() {}

  private static final String API = "/api";

  // Path Variables
  static final String VAR_USER_ID = "/{userId}";
  static final String VAR_RECIPE_ID = "/{recipeId}";
  static final String VAR_STOCK_ID = "/{stockId}";
  static final String VAR_STOCK_ITEM_ID = "/{stockItemId}";
  static final String VAR_FOOD_ID = "/{foodId}";
  static final String USER_ID = "userId";
  static final String RECIPE_ID = "recipeId";
  static final String STOCK_ID = "stockId";
  static final String STOCK_ITEM_ID = "stockItemId";
  static final String FOOD_ID = "foodId";

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

  // Stock Routes
  static final String STOCK = "/stock";
  static final String STOCK_API = API + STOCK;
  static final String STOCK_ITEM = "/stockItem";
  static final String STOCK_ITEM_API = API + STOCK_ITEM;

  // Generic Food Routes
  static final String FOOD = "/food";
  static final String FOOD_API = API + FOOD;

  // Azure Route
  static final String AZURE = API + "/azure";

  // Miscellaneous
  static final String RECS = "/recommendations";
}
