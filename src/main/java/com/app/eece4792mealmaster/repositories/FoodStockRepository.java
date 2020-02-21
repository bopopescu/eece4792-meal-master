package com.app.eece4792mealmaster.repositories;

import com.app.eece4792mealmaster.models.FoodStock;
import com.app.eece4792mealmaster.models.GenericFood;
import com.app.eece4792mealmaster.models.User;
import java.util.Collection;
import java.util.Set;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface FoodStockRepository extends CrudRepository<FoodStock, Long> {
  @Query(value = "SELECT foodStock " +
      "FROM FoodStock foodStock " +
      "WHERE foodStock.food IN :genericFoods and foodStock.user = :user", nativeQuery = true
  )
  public Collection<FoodStock> getBulkFoodStockByGenericFood(@Param("genericFoods") Set<GenericFood> genericFoods, @Param("user") User user);

  @Query(value = "SELECT foodStock " +
      "FROM FoodStock foodStock " +
      "WHERE foodStock.name LIKE :searchTerms%", nativeQuery = true
  )
  public Collection<FoodStock> searchFoodStock(@Param("searchTerms") String searchTerms);

  @Query("SELECT stock " +
          "FROM FoodStock stock " +
          "WHERE (stock.user.id=:userId AND stock.food.id=:foodId)"
  )
  public FoodStock findStockByFood(@Param("userId") Long userId, @Param("foodId") Long foodId);
}
