package com.app.eece4792mealmaster.services;

import com.app.eece4792mealmaster.models.GenericFood;
import com.app.eece4792mealmaster.repositories.GenericFoodRepository;
import java.util.ArrayList;
import java.util.Collection;
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

  public Collection<GenericFood> getGenericFoodByName(String genericFoodName) {
    return genericFoodName.equals("") ? new ArrayList<>() : genericFoodRepository.searchGenericFood(genericFoodName);
  }
}
