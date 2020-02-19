package com.app.eece4792mealmaster.controllers;

import static com.app.eece4792mealmaster.controllers.Routes.FOOD_API;
import static com.app.eece4792mealmaster.controllers.Routes.FOOD_ID;
import static com.app.eece4792mealmaster.controllers.Routes.SEARCH;
import static com.app.eece4792mealmaster.controllers.Routes.SEARCHTERMS;
import static com.app.eece4792mealmaster.controllers.Routes.VAR_FOOD_ID;

import com.app.eece4792mealmaster.services.GenericFoodService;
import com.app.eece4792mealmaster.utils.ApiResponse;

import com.app.eece4792mealmaster.utils.Views;
import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = {"*", "http://localhost:3000"}, allowCredentials = "true")
public class GenericFoodController {

  @Autowired
  private GenericFoodService genericFoodService;

  @GetMapping(FOOD_API + VAR_FOOD_ID)
  @JsonView(Views.Detailed.class)
  public ApiResponse getGenericFoodById(@PathVariable(FOOD_ID) Long foodId) {
    return new ApiResponse(genericFoodService.getGenericFoodById(foodId));
  }

  @GetMapping(FOOD_API + SEARCH)
  @JsonView(Views.Summary.class)
  public ApiResponse getGenericFoodByName(@RequestParam(SEARCHTERMS) String genericFoodName) {
    return new ApiResponse(genericFoodService.getGenericFoodByName(genericFoodName));
  }

}
