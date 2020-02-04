package com.app.eece4792mealmaster.services;

import com.app.eece4792mealmaster.constants.Consts;
import com.app.eece4792mealmaster.models.FoodStock;
import com.app.eece4792mealmaster.models.Recipe;
import com.app.eece4792mealmaster.models.StockItem;
import com.app.eece4792mealmaster.models.User;
import com.app.eece4792mealmaster.repositories.FoodStockRepository;
import com.app.eece4792mealmaster.repositories.StockItemRepository;
import com.app.eece4792mealmaster.repositories.UserRepository;
import com.app.eece4792mealmaster.utils.ApiResponse;
import com.app.eece4792mealmaster.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;
import java.util.Collection;
import java.util.Optional;

@Transactional
@Service
public class StockService {

  @Autowired
  private FoodStockRepository foodStockRepository;

  @Autowired
  private StockItemRepository stockItemRepository;

  @Autowired
  private UserRepository userRepository;

  public FoodStock getFoodStockById(Long foodStockId) {
    Optional<FoodStock> oFoodStock = foodStockRepository.findById(foodStockId);
    return oFoodStock.orElse(null);
  }

  public Collection<FoodStock> getStockByUser(Long userId) {
    Optional<User> oUser = userRepository.findById(userId);
    return oUser.<Collection<FoodStock>>map(User::getFoodStocks).orElse(null);
  }

  public StockItem getStockItemById(Long stockItemId) {
    Optional<StockItem> oStockItem = stockItemRepository.findById(stockItemId);
    return oStockItem.orElse(null);
  }

  public FoodStock addToStock(Long foodStockId, StockItem stockItem) {
    FoodStock updatedStock = this.getFoodStockById(foodStockId);
    if (updatedStock == null) return updatedStock;
    updatedStock.addStockItem(stockItem);
    foodStockRepository.save(updatedStock);
    return updatedStock;
  }

  public FoodStock createNewStock(Long foodId, StockItem stockItem) {
    // TODO;
    return null;
  }

  public FoodStock updateStockItem(StockItem stockItemData) {
    Optional<StockItem> oStockItem = stockItemRepository.findById(stockItemData.getId());
    if (!oStockItem.isPresent()) { return null; }
    StockItem newStockItem = oStockItem.get();
    Utils.updateModel(newStockItem, stockItemData);
    stockItemRepository.save(newStockItem);
    return this.getFoodStockById(stockItemData.getId());
  }

  public boolean deleteStockItem(Long stockItemId) {
    Optional<StockItem> toDelete = stockItemRepository.findById(stockItemId);
    toDelete.ifPresent(stockItem -> stockItemRepository.delete(stockItem));
    return toDelete.isPresent();
  }
 }
