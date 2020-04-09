package com.app.eece4792mealmaster.services;

import com.app.eece4792mealmaster.dto.MealDto;
import com.app.eece4792mealmaster.dto.MealFoodDto;
import com.app.eece4792mealmaster.dto.MealRecipeDto;
import com.app.eece4792mealmaster.models.*;
import com.app.eece4792mealmaster.repositories.FoodStockRepository;
import com.app.eece4792mealmaster.repositories.RecipeRepository;
import com.app.eece4792mealmaster.repositories.StockItemRepository;
import com.app.eece4792mealmaster.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.transaction.Transactional;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Transactional
@Service
public class MealService {
    @Autowired
    UserRepository userRepository;

    @Autowired
    RecipeRepository recipeRepository;

    @Autowired
    FoodStockRepository foodStockRepository;

    @Autowired
    StockItemRepository stockItemRepository;

    public void consume(Long userId, MealDto mealData) {
        Optional<User> oUser = userRepository.findById(userId);
        if (!oUser.isPresent()) throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        Map<Long, FoodStock> stocks = oUser.get().getFoodStocks().stream()
                .collect(Collectors.toMap(
                        FoodStock::getFoodId,
                        Function.identity())
                );
        Map<Long, Double> toConsume = new HashMap<>();
        for (MealFoodDto cFood : mealData.getFoods()) {
            if (toConsume.containsKey(cFood.getId())) {
                toConsume.replace(cFood.getId(), toConsume.get(cFood.getId()) + cFood.getServings());
            } else {
                toConsume.put(cFood.getId(), cFood.getServings());
            }
        }
        for (MealRecipeDto cRecipe : mealData.getRecipes()) {
            Optional<Recipe> oRecipe = recipeRepository.findById(cRecipe.getId());
            if (!oRecipe.isPresent()) return; // should be proper error handling but relying on FE to not fuck up here
            Collection<RecipeIngredient> cRecipeIngredients = oRecipe.get().getRecipeIngredients();
            for (RecipeIngredient ingredient : cRecipeIngredients) {
                Long foodId = ingredient.getIngredient().getId();
                if (toConsume.containsKey(foodId)) {
                    toConsume.replace(foodId, toConsume.get(foodId) + cRecipe.getServings() * ingredient.getServings());
                } else {
                    toConsume.put(foodId, cRecipe.getServings() * ingredient.getServings());
                }
            }
        }
        for (Map.Entry<Long, Double> entry : toConsume.entrySet()) {
            if (stocks.get(entry.getKey()).getTotalQuantity() <= entry.getValue()) {
                // delete the stock entirely
                foodStockRepository.delete(stocks.get(entry.getKey()));
            } else {
                // determine which stock items to delete
                Double leftToConsume = entry.getValue();
                FoodStock stock = stocks.get(entry.getKey());
                List<StockItem> stockItems = new ArrayList<>(stock.getStockItems());
                stockItems.sort(Comparator.comparing(StockItem::getExpirationDate));
                for (StockItem si : stockItems) {
                    if (si.getQuantity() <= leftToConsume) {
                        leftToConsume -= si.getQuantity();
                        stockItemRepository.deleteById(si.getId());
                    } else {
                        si.setQuantity(si.getQuantity() - leftToConsume);
                        stockItemRepository.save(si);
                        break;
                    }
                }
            }
        }
    }
}
