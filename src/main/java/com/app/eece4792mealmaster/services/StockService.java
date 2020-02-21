package com.app.eece4792mealmaster.services;

import com.app.eece4792mealmaster.models.FoodStock;
import com.app.eece4792mealmaster.models.GenericFood;
import com.app.eece4792mealmaster.models.StockItem;
import com.app.eece4792mealmaster.models.User;
import com.app.eece4792mealmaster.repositories.FoodStockRepository;
import com.app.eece4792mealmaster.repositories.GenericFoodRepository;
import com.app.eece4792mealmaster.repositories.StockItemRepository;
import com.app.eece4792mealmaster.repositories.UserRepository;
import com.app.eece4792mealmaster.utils.Utils;

import java.time.LocalDate;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
  private GenericFoodRepository genericFoodRepository;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private GenericFoodService genericFoodService;

  public FoodStock getFoodStockById(Long foodStockId) {
    Optional<FoodStock> oFoodStock = foodStockRepository.findById(foodStockId);
    return oFoodStock.orElse(null);
  }

  public FoodStock getFoodStockByFood(Long userId, Long foodId) {
    return foodStockRepository.findStockByFood(userId, foodId);
  }

  public Collection<FoodStock> getStockByUser(Long userId) {
    Optional<User> oUser = userRepository.findById(userId);
    return oUser.<Collection<FoodStock>>map(User::getFoodStocks).orElse(null);
  }

  public StockItem getStockItemById(Long stockItemId) {
    Optional<StockItem> oStockItem = stockItemRepository.findById(stockItemId);
    return oStockItem.orElse(null);
  }

  public FoodStock addToStock(Long foodId, StockItem stockItem, Long userId) {
    
    FoodStock stock = foodStockRepository.findStockByFood(userId, foodId);
    if (stock == null) {
      stock = new FoodStock();
      GenericFood food = genericFoodRepository.findById(foodId).orElse(null);
      User user = userRepository.findById(userId).orElse(null);
      if (food == null || user == null) return null;
      stock.setFood(food);
      stock.setUser(user);

    }
    foodStockRepository.save(stock);

    stockItem.setFoodStock(stock);
    stockItem.setDateObtained(LocalDate.now());
    if (stockItem.getExpirationDate() == null) {
      stockItem.setExpirationDate(stockItem.getDateObtained().plusDays((int)(Math.random() * 14)));
    }
    stockItemRepository.save(stockItem);
    stock.addStockItem(stockItem);
    return stock;
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

  public Collection<FoodStock> getFoodStockByName(String foodStockName) {
    return foodStockName.equals("") ? new ArrayList<>() : foodStockRepository.searchFoodStock(foodStockName);
  }

}
