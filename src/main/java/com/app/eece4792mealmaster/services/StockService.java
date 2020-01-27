package com.app.eece4792mealmaster.services;

import com.app.eece4792mealmaster.constants.Consts;
import com.app.eece4792mealmaster.models.FoodStock;
import com.app.eece4792mealmaster.models.User;
import com.app.eece4792mealmaster.repositories.StockRepository;
import com.app.eece4792mealmaster.repositories.UserRepository;
import com.app.eece4792mealmaster.utils.ApiResponse;
import com.app.eece4792mealmaster.utils.Utils;
import com.app.eece4792mealmaster.utils.exceptions.BadRequest;
import com.app.eece4792mealmaster.utils.exceptions.ForbiddenRequestException;
import com.app.eece4792mealmaster.utils.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;
import java.util.Optional;

@Transactional
@Service
public class StockService {

  @Autowired
  private StockRepository stockRepository;

  @Autowired
  private UserRepository userRepository;

  public ApiResponse getStockById(Long foodStockId) {
    if (foodStockId == null) {
      throw new BadRequest();
    }
    Optional<FoodStock> oFoodStock = stockRepository.findById(foodStockId);
    if (!oFoodStock.isPresent()) {
      throw new ResourceNotFoundException();
    }
    return new ApiResponse(oFoodStock.get());
  }

  public ApiResponse getStockByUser(Long userId) {
    if (userId == null) {
      throw new BadRequest();
    }
    Optional<User> oUser = userRepository.findById(userId);
    if (!oUser.isPresent()) {
      throw new ResourceNotFoundException();
    }
    return new ApiResponse(oUser.get().getFoodStocks());
  }

  public ApiResponse createStock(HttpSession session, FoodStock foodStock) {
    Long userId = (Long)(session.getAttribute(Consts.SessionConsts.USER_ID));
    if (userId == null || foodStock == null) {
      throw new BadRequest();
    }
    Optional<User> oUser = userRepository.findById(userId);
    if (!oUser.isPresent()) {
      throw new ResourceNotFoundException();
    }
    foodStock.setUser(oUser.get());
    stockRepository.save(foodStock);
    return new ApiResponse(foodStock);
  }

  public ApiResponse updateStock(HttpSession session, FoodStock foodStock) {
    Long userId = (Long)(session.getAttribute(Consts.SessionConsts.USER_ID));
    if (userId == null || foodStock == null) { throw new BadRequest(); }
    Optional<FoodStock> oFoodStock = stockRepository.findById(foodStock.getId());
    if (!oFoodStock.isPresent()) {
      throw new ResourceNotFoundException();
    }
    FoodStock newFoodStock = oFoodStock.get();
    if (!newFoodStock.getUser().getId().equals(userId)) {
      throw new ForbiddenRequestException();
    }
    Utils.updateModel(newFoodStock, foodStock);
    stockRepository.save(newFoodStock);
    return new ApiResponse(newFoodStock);
  }

  public ApiResponse deleteStock(HttpSession session, Long foodStockId) {
    Long userId = (Long)(session.getAttribute(Consts.SessionConsts.USER_ID));
    Optional<FoodStock> oFoodStock = stockRepository.findById(foodStockId);
    if (userId == null || foodStockId == null || !oFoodStock.isPresent()) {
      throw new ResourceNotFoundException();
    }
    if (!oFoodStock.get().getUser().getId().equals(userId)) {
      throw new ForbiddenRequestException();
    }
    stockRepository.deleteById(foodStockId);
    return new ApiResponse();
  }
}
