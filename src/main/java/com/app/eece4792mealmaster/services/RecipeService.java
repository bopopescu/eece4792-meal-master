package com.app.eece4792mealmaster.services;

import com.app.eece4792mealmaster.models.Recipe;
import com.app.eece4792mealmaster.models.User;
import com.app.eece4792mealmaster.repositories.RecipeRepository;
import com.app.eece4792mealmaster.repositories.UserRepository;
import com.app.eece4792mealmaster.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

@Transactional
@Service
public class RecipeService {
    @Autowired
    private RecipeRepository recipeRepository;

    @Autowired
    private UserRepository userRepository;

    public Collection<Recipe> searchRecipes(String searchTerms) {
        return searchTerms.equals("") ? new ArrayList<>() : recipeRepository.searchRecipes(searchTerms);
    }

    public Collection<Recipe> getRecipeByUser(Long userId) {
        Optional<User> oUser = userRepository.findById(userId);
        return oUser.<Collection<Recipe>>map(User::getCreatedRecipes).orElse(null);
    }

    public Recipe createRecipe(Long userId, Recipe recipe) {
        Optional<User> oUser = userRepository.findById(userId);
        if (!oUser.isPresent()) {
            return null;
        }
        recipe.setCreator(oUser.get());
        recipeRepository.save(recipe);
        return recipe;
    }

    public Recipe updateRecipe(Recipe recipeData) {
        Recipe newRecipe = this.findById(recipeData.getId());
        if (newRecipe == null) return newRecipe;
        Utils.updateModel(newRecipe, recipeData);
        recipeRepository.save(newRecipe);
        return newRecipe;
    }

    public Recipe findById(Long recipeId) {
        return recipeRepository.findById(recipeId).orElse(null);
    }

    public boolean deleteRecipe(Long recipeId) {
        Optional<Recipe> toDelete = recipeRepository.findById(recipeId);
        toDelete.ifPresent(recipe -> recipeRepository.delete(recipe));
        return toDelete.isPresent();
    }
}
