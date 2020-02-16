package com.app.eece4792mealmaster.repositories;

import com.app.eece4792mealmaster.models.FoodStock;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface FoodStockRepository extends CrudRepository<FoodStock, Long> {
  @Query("SELECT stock " +
          "FROM FoodStock stock " +
          "WHERE (stock.user.id=:userId AND stock.food.id=:foodId)"
  )
  public FoodStock findStockByFood(@Param("userId") Long userId, @Param("foodId") Long foodId);
}
