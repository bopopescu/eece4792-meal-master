package com.app.eece4792mealmaster.controllers;

import com.app.eece4792mealmaster.models.Recipe;
import com.app.eece4792mealmaster.services.RecipeService;
import com.app.eece4792mealmaster.utils.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

import static com.app.eece4792mealmaster.controllers.Routes.*;

// TODO: Add EC2 hostname to allowed origins
@RestController
@CrossOrigin(origins = {"http://localhost:3000"}, allowCredentials = "true")
public class RecipeController {
    @Autowired
    private RecipeService recipeService;

    @GetMapping(RECIPE_API + SEARCHTERMS)
    public ApiResponse searchRecipes(@PathVariable(SEARCHTERMS) String searchTerms) {
        return recipeService.searchRecipes(searchTerms);
    }

    @GetMapping(USER_API + USER_ID + RECIPE)
    public ApiResponse getUserRecipes(@PathVariable(USER_ID) Long userId) {
        return recipeService.getRecipeByUser(userId);
    }

    @PostMapping(RECIPE_API)
    public ApiResponse createRecipe(HttpSession session, @RequestBody Recipe payload) {
        return recipeService.createRecipe(session, payload);
    }

    @PutMapping(RECIPE_API)
    public ApiResponse updateRecipe(HttpSession session, @RequestBody Recipe payload) {
        return recipeService.updateRecipe(session, payload);
    }

    @DeleteMapping(RECIPE_API + USER_ID)
    public ApiResponse deleteRecipe(HttpSession session, @PathVariable(RECIPE_ID) Long recipeId) {
        return recipeService.deleteRecipe(session, recipeId);
    }
}
