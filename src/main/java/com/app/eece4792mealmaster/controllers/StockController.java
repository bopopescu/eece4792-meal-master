package com.app.eece4792mealmaster.controllers;

import static com.app.eece4792mealmaster.controllers.Routes.*;

import com.app.eece4792mealmaster.models.FoodStock;
import com.app.eece4792mealmaster.models.StockItem;
import com.app.eece4792mealmaster.services.StockService;
import com.app.eece4792mealmaster.utils.ApiResponse;

import javax.servlet.http.HttpSession;

import com.app.eece4792mealmaster.utils.Utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@CrossOrigin(origins = {"*", "http://localhost:3000"}, allowCredentials = "true")
public class StockController {

  @Autowired
  private StockService stockService;

  @GetMapping(STOCK_API + VAR_STOCK_ID)
  public ApiResponse getStockById(HttpSession session, @RequestParam(STOCK_ID) Long stockId) {
    Long userId = Utils.getLoggedInUser(session);
    if (userId == null) {
      throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
    }
    return new ApiResponse(stockService.getFoodStockById(stockId));
  }

  @GetMapping(FOOD_API + VAR_FOOD_ID + STOCK)
  public ApiResponse getStockByFood(HttpSession session, @RequestParam(FOOD_ID) Long foodId) {
    throw new ResponseStatusException(HttpStatus.NOT_IMPLEMENTED);
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
  public ApiResponse getStockItemById(HttpSession session, @RequestParam(STOCK_ITEM_ID) Long stockItemId) {
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
  public ApiResponse addToStock(HttpSession session, @RequestParam(FOOD_ID) Long foodId, @RequestBody StockItem payload) {
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
  public ApiResponse deleteStock(HttpSession session, @RequestParam(STOCK_ITEM_ID) Long stockItemId) {
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
