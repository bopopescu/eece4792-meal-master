package com.app.eece4792mealmaster.controllers;

import com.app.eece4792mealmaster.dto.RecipeDto;
import com.app.eece4792mealmaster.dto.RecipeIngredientDto;
import com.app.eece4792mealmaster.models.FoodStock;
import com.app.eece4792mealmaster.models.GenericFood;
import com.app.eece4792mealmaster.models.Recipe;
import com.app.eece4792mealmaster.models.RecipeIngredient;
import com.app.eece4792mealmaster.services.RecipeService;
import com.app.eece4792mealmaster.services.StockService;
import com.app.eece4792mealmaster.utils.ApiResponse;
import com.app.eece4792mealmaster.utils.Utils;
import com.app.eece4792mealmaster.utils.Views;
import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpSession;

import java.text.ParseException;
import java.util.*;

import static com.app.eece4792mealmaster.constants.Routes.*;

// TODO: Add EC2 hostname to allowed origins
@RestController
@CrossOrigin(origins = {"*", "http://localhost:3000"}, allowCredentials = "true")
public class RecipeController {
    @Autowired
    private RecipeService recipeService;

    @Autowired
    private StockService stockService;

    @JsonView(Views.Summary.class)
    @GetMapping(RECIPE_API + SEARCH)
    public ApiResponse searchRecipes(@RequestParam(SEARCHTERMS) String searchTerms) {
        return new ApiResponse(recipeService.searchRecipes(searchTerms));
    }

    @JsonView(Views.Summary.class)
    @GetMapping(USER_API + VAR_USER_ID + RECIPE)
    public ApiResponse getUserRecipes(@PathVariable(USER_ID) Long userId) {
        if (userId == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        return new ApiResponse(recipeService.getRecipeByUser(userId));
    }

    @JsonView(Views.Detailed.class)
    @GetMapping(RECIPE_API + VAR_RECIPE_ID)
    public ApiResponse getRecipe(@PathVariable(RECIPE_ID) Long recipeId) {
        RecipeDto recipe = recipeService.findById(recipeId);
        if (recipe == null) throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        return new ApiResponse(recipe);
    }

    @JsonView(Views.Summary.class)
    @GetMapping(RECIPE_API + RECS)
    public ApiResponse getRecipeRecommendations(HttpSession session) {
        Long userId = Utils.getLoggedInUser(session);
        if (userId == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
        return new ApiResponse(recipeService.getRecipeRecs(userId));
    }

    @JsonView(Views.Detailed.class)
    @PostMapping(RECIPE_API)
    public ApiResponse createRecipe(HttpSession session, @RequestBody RecipeDto payload) {
        Long userId = Utils.getLoggedInUser(session);
        if (userId == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
        if (payload == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        try {
            RecipeDto createdRecipe = recipeService.createRecipe(userId, payload);
            if (createdRecipe == null) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND);
            }
            return new ApiResponse(createdRecipe);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, e.toString());
        }
    }

    @JsonView(Views.Detailed.class)
    @PutMapping(RECIPE_API)
    public ApiResponse updateRecipe(HttpSession session, @RequestBody RecipeDto recipeData) {
        Long userId = Utils.getLoggedInUser(session);
        if (userId == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
        if (recipeData == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        if (recipeData.getId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Unspecified Recipe identifier");
        }
        recipeData.setCreator(userId);
        RecipeDto oldRecipe = recipeService.findById(recipeData.getId());
        if (!oldRecipe.getCreator().equals(userId)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
        try {
            RecipeDto updatedRecipe = recipeService.updateRecipe(recipeData);
            if (updatedRecipe == null) {
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
            }
            return new ApiResponse(updatedRecipe);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, e.toString());
        }
    }

    @JsonView(Views.Detailed.class)
    @DeleteMapping(RECIPE_API + VAR_RECIPE_ID)
    public ApiResponse deleteRecipe(HttpSession session, @PathVariable(RECIPE_ID) Long recipeId) {
        RecipeDto toDelete = recipeService.findById(recipeId);
        if (toDelete == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        Long userId = Utils.getLoggedInUser(session);
        if (userId == null || !userId.equals(toDelete.getCreator())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
        recipeService.deleteRecipe(recipeId);
        return new ApiResponse(String.format("Recipe %d deleted", recipeId));
    }

    @JsonView(Views.Summary.class)
    @GetMapping(RECIPE_API + LIKE)
    public ApiResponse getLikedRecipes(HttpSession session) {
        Long userId = Utils.getLoggedInUser(session);
        if (userId == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
        return new ApiResponse(recipeService.getUserLikedRecipes(userId));
    }

    @JsonView(Views.Summary.class)
    @PostMapping(RECIPE_API + LIKE + VAR_RECIPE_ID)
    public ApiResponse likeRecipe(HttpSession session, @PathVariable(RECIPE_ID) Long recipeId) {
        Long userId = Utils.getLoggedInUser(session);
        if (userId == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
        return new ApiResponse(recipeService.likeRecipe(userId, recipeId));
    }

    @JsonView(Views.Summary.class)
    @DeleteMapping(RECIPE_API + LIKE + VAR_RECIPE_ID)
    public ApiResponse unlikeRecipe(HttpSession session, @PathVariable(RECIPE_ID) Long recipeId) {
        Long userId = Utils.getLoggedInUser(session);
        if (userId == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
        return new ApiResponse(recipeService.unlikeRecipe(userId, recipeId));
    }

    //SHOPPING_LIST
    @GetMapping(SHOPPING_LIST)
    public ApiResponse getMissingItems(HttpSession session, @PathVariable(RECIPE_ID) Long recipeId) {
        Long userId = Utils.getLoggedInUser(session);
        if (userId == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
        Recipe recipe;
        try{
            recipe = recipeService.convertToEntity(recipeService.findById(recipeId));
        } catch (ParseException e) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY);
        }

        // Put user's food stock into a set of generic foods
        Collection<FoodStock> foodStocks = stockService.getStockByUser(userId);
        Set<GenericFood> usersGenericFoods = new HashSet<GenericFood>();
        for(FoodStock foodStock : foodStocks)
            usersGenericFoods.add(foodStock.getFood());

        // Get all the user's missing items needed for recipe
        List<GenericFood> missingItems = new ArrayList<>();
        for(RecipeIngredient ingredient : recipe.getRecipeIngredients())
        {
            // Check if user is missing ingredient in their food stock
            if(!usersGenericFoods.contains(ingredient.getIngredient()))
            {
                missingItems.add(ingredient.getIngredient());
            }
        }

        return new ApiResponse(missingItems);
    }
}
