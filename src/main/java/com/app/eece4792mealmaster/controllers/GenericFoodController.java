package com.app.eece4792mealmaster.controllers;

import static com.app.eece4792mealmaster.controllers.Routes.FOOD_API;
import static com.app.eece4792mealmaster.controllers.Routes.FOOD_ID;

import com.app.eece4792mealmaster.services.GenericFoodService;
import com.app.eece4792mealmaster.utils.ApiResponse;
import com.app.eece4792mealmaster.utils.Utils;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@CrossOrigin(origins = {"*", "http://localhost:3000"}, allowCredentials = "true")
public class GenericFoodController {

  @Autowired
  private GenericFoodService genericFoodService;

  @GetMapping(FOOD_API + FOOD_ID)
  public ApiResponse getGenericFoodById(HttpSession session, @RequestParam(FOOD_ID) Long foodId) {
    Long userId = Utils.getLoggedInUser(session);
    if (userId == null) {
      throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
    }
    return new ApiResponse(genericFoodService.getGenericFoodById(foodId));
  }
}
