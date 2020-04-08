package com.app.eece4792mealmaster.constants;

public final class Routes {
  private Routes() {}

  private static final String API = "/api";

  public static final String S3_URL = "https://meal-master-imgs.s3.amazonaws.com";

  public static final String S3_IMG_EXT = ".jpg";

  // Path Variables
  public static final String VAR_USER_ID = "/{userId}";
  public static final String VAR_RECIPE_ID = "/{recipeId}";
  public static final String VAR_STOCK_ID = "/{stockId}";
  public static final String VAR_STOCK_ITEM_ID = "/{stockItemId}";
  public static final String VAR_FOOD_ID = "/{foodId}";
  public static final String USER_ID = "userId";
  public static final String RECIPE_ID = "recipeId";
  public static final String STOCK_ID = "stockId";
  public static final String STOCK_ITEM_ID = "stockItemId";
  public static final String FOOD_ID = "foodId";

  // User Routes
  public static final String USER = "/user";
  public static final String USER_API = API + USER;
  public static final String LOGIN = USER_API + "/login";
  public static final String LOGOUT = USER_API + "/logout";
  public static final String REGISTER = USER_API + "/register";
  public static final String PROFILE = USER_API + "/profile";
  public static final String INSIGHTS = USER_API + "/insights";

  // Product Routes
  public static final String PRODUCT = "/product";
  public static final String PRODUCT_API = API + PRODUCT;

  // Recipe Routes
  public static final String RECIPE = "/recipe";
  public static final String RECIPE_API = API + RECIPE;
  public static final String LIKE = "/likes";

  // Query Params
  public static final String SEARCH = "/search";
  public static final String SEARCHTERMS = "searchTerms";

  // Stock Routes
  public static final String STOCK = "/stock";
  public static final String STOCK_API = API + STOCK;
  public static final String STOCK_ITEM = "/stockItem";
  public static final String STOCK_ITEM_API = API + STOCK_ITEM;

  // Generic Food Routes
  public static final String FOOD = "/food";
  public static final String FOOD_API = API + FOOD;

  // Meal Routes
  public static final String MEAL = "/meal";
  public static final String MEAL_API = API + MEAL;
  public static final String CONSUME = "/consume";

  // Azure Route
  public static final String AZURE = API + "/azure";

  // Miscellaneous
  public static final String RECS = "/recommendations";
}
