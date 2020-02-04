package com.app.eece4792mealmaster.repositories;

import com.app.eece4792mealmaster.models.StockItem;
import org.springframework.data.repository.CrudRepository;

public interface StockItemRepository extends CrudRepository<StockItem, Long> {
}
