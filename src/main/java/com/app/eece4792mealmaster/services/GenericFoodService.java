package com.app.eece4792mealmaster.services;

import com.app.eece4792mealmaster.models.FoodStock;
import com.app.eece4792mealmaster.models.GenericFood;
import com.app.eece4792mealmaster.repositories.GenericFoodRepository;
import com.sun.tools.javac.jvm.Gen;
import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Transactional
@Service
public class GenericFoodService {

  @Autowired
  private GenericFoodRepository genericFoodRepository;

  public GenericFood getGenericFoodById(Long genericFoodId) {
    Optional<GenericFood> oGenericFood = genericFoodRepository.findById(genericFoodId);
    return oGenericFood.orElse(null);
  }

  public FoodStock getFoodStockByGenericFood(Long genericFoodId) {
    GenericFood genericFood = getGenericFoodById(genericFoodId);
    return genericFood.getFoodStock();
  }
}
