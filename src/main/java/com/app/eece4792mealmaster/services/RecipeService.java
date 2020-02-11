package com.app.eece4792mealmaster.services;

import com.app.eece4792mealmaster.models.FoodStock;
import com.app.eece4792mealmaster.models.GenericFood;
import com.app.eece4792mealmaster.models.Recipe;
import com.app.eece4792mealmaster.models.RecipeIngredient;
import com.app.eece4792mealmaster.models.User;
import com.app.eece4792mealmaster.repositories.FoodStockRepository;
import com.app.eece4792mealmaster.repositories.RecipeRepository;
import com.app.eece4792mealmaster.repositories.UserRepository;
import com.app.eece4792mealmaster.utils.Utils;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
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

    @Autowired
    private FoodStockRepository foodStockRepository;

    private StockService stockService;

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

    /**
     * Determines if this recipe this can be made
     */
    public boolean canRecipeBeMade(Recipe recipe, User user) {
        Map<GenericFood, Double> ingredientsAndServings = recipe.getIngredients().stream()
            .collect(Collectors.toMap(
                RecipeIngredient::getIngredient,
                RecipeIngredient::getServings)
            );

        // we will make a map of Generic Food Id -> the number of servings that are
        // required to make a recipe for given generic food AKA it's corresponding foodstock
        Map<Long, Double> requiredServingsByGenericFood = new HashMap<>();
        ingredientsAndServings.forEach((k, v) -> requiredServingsByGenericFood.put(k.getId(), v));

        Collection<FoodStock> foodStocks = foodStockRepository.getBulkFoodStockByGenericFood(requiredServingsByGenericFood.keySet(), user.getId());
        Map<Long, FoodStock> foodStockByGenericFoodId = foodStocks.stream().collect(
            Collectors.toMap(FoodStock::getGenericFoodId, Function.identity()));

        for(Map.Entry<Long, FoodStock> entry : foodStockByGenericFoodId.entrySet()) {
            FoodStock currentIngredient = entry.getValue();
            double availableServings = stockService.getTotalQuantity(currentIngredient);
            Long genericFoodId = entry.getKey();
            // if the amount that we need is more than what we have, return false
            if (requiredServingsByGenericFood.get(genericFoodId) > availableServings) {
                return false;
            }
        }
        return true;
    }
}
