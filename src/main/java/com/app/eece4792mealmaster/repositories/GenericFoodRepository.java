package com.app.eece4792mealmaster.repositories;

import com.app.eece4792mealmaster.models.GenericFood;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface GenericFoodRepository extends CrudRepository<GenericFood, Long> {
  @Query("SELECT genericFood " +
      "FROM GenericFood genericFood " +
      "WHERE genericFood.name LIKE :searchTerms%"
  )
  public List<GenericFood> searchGenericFood(@Param("searchTerms") String searchTerms, Pageable pageable);
}

