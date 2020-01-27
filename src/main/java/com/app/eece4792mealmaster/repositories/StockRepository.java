package com.app.eece4792mealmaster.repositories;

import com.app.eece4792mealmaster.models.FoodStock;
import org.springframework.data.repository.CrudRepository;

public interface StockRepository extends CrudRepository<FoodStock, Long> {

}
