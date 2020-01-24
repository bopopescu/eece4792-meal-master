package com.app.eece4792mealmaster.repositories;

import com.app.eece4792mealmaster.models.Recipe;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Collection;

public interface RecipeRepository extends CrudRepository<Recipe, Long> {
    @Query("SELECT recipe " +
            "FROM Recipe recipe " +
            "WHERE recipe.name LIKE :searchTerms%"
    )
    public Collection<Recipe> searchRecipes(@Param("searchTerms") String searchTerms);
}
