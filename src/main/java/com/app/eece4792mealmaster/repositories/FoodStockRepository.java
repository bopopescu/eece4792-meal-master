package com.app.eece4792mealmaster.repositories;

import com.app.eece4792mealmaster.models.FoodStock;
import java.util.Collection;
import java.util.Set;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface FoodStockRepository extends CrudRepository<FoodStock, Long> {
  @Query("SELECT foodStock " +
      "FROM FoodStock foodStock " +
      "WHERE foodStock.genericFoodId IN ?1 and foodstock.userId = :userId"
  )
  public Collection<FoodStock> getBulkFoodStockByGenericFood(@Param("genericFoodIds") Set<Long> genericFoodIds, @Param("userId") long userId);

  @Query("SELECT foodStock " +
      "FROM FoodStock foodStock " +
      "WHERE foodStock.name LIKE :searchTerms%"
  )
  public Collection<FoodStock> searchFoodStock(@Param("searchTerms") String searchTerms);
}
