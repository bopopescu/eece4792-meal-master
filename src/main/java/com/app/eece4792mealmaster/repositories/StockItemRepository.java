package com.app.eece4792mealmaster.repositories;

import com.app.eece4792mealmaster.models.StockItem;

import java.util.Set;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface StockItemRepository extends CrudRepository<StockItem, Long> {

  @Modifying
  @Query("DELETE FROM StockItem stockItem WHERE stockItem.id IN :stockItemIds")
  public void deleteStockItems(@Param("stockItemIds") Set<Long> stockItemIds);
}
