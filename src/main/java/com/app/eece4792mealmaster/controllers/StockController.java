package com.app.eece4792mealmaster.controllers;

import static com.app.eece4792mealmaster.controllers.Routes.*;

import com.app.eece4792mealmaster.models.FoodStock;
import com.app.eece4792mealmaster.services.StockService;
import com.app.eece4792mealmaster.utils.ApiResponse;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = {"*", "http://localhost:3000"}, allowCredentials = "true")
public class StockController {

    @Autowired
    private StockService stockService;

  @GetMapping(STOCK_API + VAR_STOCK_ID)
  public ApiResponse getStockById(@RequestParam(STOCK_ID) Long stockId) {
    return stockService.getStockById(stockId);
  }

    @GetMapping(USER_API + VAR_USER_ID + STOCK)
    public ApiResponse getUserStock(@RequestParam(USER_ID) Long userId) {
      return stockService.getStockByUser(userId);
    }

    @PostMapping(STOCK_API)
    public ApiResponse createStock(HttpSession session, @RequestBody FoodStock payload) {
      return stockService.createStock(session, payload);
    }

    @PutMapping(STOCK_API)
    public ApiResponse updateStock(HttpSession session, @RequestBody FoodStock payload) {
      return stockService.updateStock(session, payload);
    }

    @DeleteMapping(STOCK_API + VAR_STOCK_ID)
    public ApiResponse deleteStock(HttpSession session, @RequestParam(STOCK_ID) Long stockId) {
      return stockService.deleteStock(session, stockId);
    }
}
