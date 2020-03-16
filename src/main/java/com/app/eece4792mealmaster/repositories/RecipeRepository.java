package com.app.eece4792mealmaster.repositories;

import com.app.eece4792mealmaster.models.Recipe;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.domain.Pageable;

public interface RecipeRepository extends CrudRepository<Recipe, Long> {
    @Query("SELECT recipe " +
            "FROM Recipe recipe " +
            "WHERE recipe.name LIKE :searchTerms%"
    )
    public List<Recipe> searchRecipes(@Param("searchTerms") String searchTerms, Pageable pageable);

    @Query("SELECT recipe " +
            "FROM Recipe recipe " +
            "WHERE function('recipeCanBeMade', :userId, recipe.id) >= 1"
    )
    public List<Recipe> findRecipeRecs(@Param("userId") Long userId, Pageable pageable);
}
