package com.app.eece4792mealmaster.controllers;

import static com.app.eece4792mealmaster.controllers.Routes.*;

import com.app.eece4792mealmaster.models.StockItem;
import com.app.eece4792mealmaster.services.GenericFoodService;
import com.app.eece4792mealmaster.services.StockService;
import com.app.eece4792mealmaster.utils.ApiResponse;

import javax.servlet.http.HttpSession;

import com.app.eece4792mealmaster.utils.Utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@CrossOrigin(origins = {"*", "http://localhost:3000"}, allowCredentials = "true")
public class StockController {

  @Autowired
  private StockService stockService;

  @Autowired
  private GenericFoodService genericFoodService;
  
  @GetMapping(STOCK_API + VAR_STOCK_ID)
  public ApiResponse getStockById(HttpSession session, @PathVariable(STOCK_ID) Long stockId) {
    Long userId = Utils.getLoggedInUser(session);
    if (userId == null) {
      throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
    }
    return new ApiResponse(stockService.getFoodStockById(stockId));
  }

  @GetMapping(FOOD_API + VAR_FOOD_ID + STOCK)
  public ApiResponse getStockByFood(HttpSession session, @PathVariable(FOOD_ID) Long foodId) {
      // TO SEAN: I implemented this stub that you had - let me know if you were expecting this functionality
      // it returns the corresponding food stock for this generic food
      Long userId = Utils.getLoggedInUser(session);
      if (userId == null) {
          throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
      }
      return new ApiResponse(genericFoodService.getGenericFoodById(foodId).getStocks());
     // throw new ResponseStatusException(HttpStatus.NOT_IMPLEMENTED);
  }

  @GetMapping(STOCK_API)
  public ApiResponse getUserStock(HttpSession session) {
    Long userId = Utils.getLoggedInUser(session);
    if (userId == null) {
      throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
    }

    return new ApiResponse(stockService.getStockByUser(userId));
  }

  @GetMapping(STOCK_ITEM_API + VAR_STOCK_ITEM_ID)
  public ApiResponse getStockItemById(HttpSession session, @PathVariable(STOCK_ITEM_ID) Long stockItemId) {
    Long userId = Utils.getLoggedInUser(session);
    if (userId == null) {
      throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
    }
    return new ApiResponse(stockService.getStockItemById(stockItemId));
  }

  // DO NOT USE
//  @PostMapping(STOCK_ITEM_API + VAR_STOCK_ID)
//  public ApiResponse addStockItem(HttpSession session, @RequestParam(STOCK_ID) Long stockId, @RequestBody StockItem payload) {
//    Long userId = Utils.getLoggedInUser(session);
//    if (userId == null) {
//      throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
//    }
//    return new ApiResponse(stockService.addToStock(stockId, payload));
//  }

  @PostMapping(STOCK_ITEM_API + FOOD + VAR_FOOD_ID)
  public ApiResponse addToStock(HttpSession session, @PathVariable(FOOD_ID) Long foodId, @RequestBody StockItem payload) {
    Long userId = Utils.getLoggedInUser(session);
    if (userId == null) {
      throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
    }
    return new ApiResponse(stockService.addToStock(foodId, payload, userId));
  }

  @PutMapping(STOCK_ITEM_API)
  public ApiResponse updateStockItem(HttpSession session, @RequestBody StockItem payload) {
    Long userId = Utils.getLoggedInUser(session);
    if (userId == null) {
      throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
    }
    return new ApiResponse(stockService.updateStockItem(payload));
  }

  @DeleteMapping(STOCK_ITEM_API + VAR_STOCK_ITEM_ID)
  public ApiResponse deleteStock(HttpSession session, @PathVariable(STOCK_ITEM_ID) Long stockItemId) {
    Long userId = Utils.getLoggedInUser(session);
    if (userId == null) {
      throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
    }
    if (!stockService.deleteStockItem(stockItemId)) {
      throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY);
    }
    return new ApiResponse(String.format("StockItem %d deleted", stockItemId));
  }
}
