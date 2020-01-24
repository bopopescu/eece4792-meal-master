package com.app.eece4792mealmaster.controllers;

import com.app.eece4792mealmaster.constants.Consts;
import com.app.eece4792mealmaster.models.Recipe;
import com.app.eece4792mealmaster.services.RecipeService;
import com.app.eece4792mealmaster.utils.ApiResponse;
import com.app.eece4792mealmaster.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpSession;

import static com.app.eece4792mealmaster.controllers.Routes.*;

// TODO: Add EC2 hostname to allowed origins
@RestController
@CrossOrigin(origins = {"*", "http://localhost:3000"}, allowCredentials = "true")
public class RecipeController {
    @Autowired
    private RecipeService recipeService;

    @GetMapping(RECIPE_API + SEARCH)
    public ApiResponse searchRecipes(@RequestParam(SEARCHTERMS) String searchTerms) {
        return new ApiResponse(recipeService.searchRecipes(searchTerms));
    }

    @GetMapping(USER_API + VAR_USER_ID + RECIPE)
    public ApiResponse getUserRecipes(@PathVariable(USER_ID) Long userId) {
        if (userId == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        return new ApiResponse(recipeService.getRecipeByUser(userId));
    }

    @PostMapping(RECIPE_API)
    public ApiResponse createRecipe(HttpSession session, @RequestBody Recipe payload) {
        Long userId = Utils.getLoggedInUser(session);
        if (userId == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
        if (payload == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        Recipe createdRecipe = recipeService.createRecipe(userId, payload);
        if (createdRecipe == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        return new ApiResponse(createdRecipe);
    }

    @PutMapping(RECIPE_API)
    public ApiResponse updateRecipe(HttpSession session, @RequestBody Recipe recipeData) {
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
        Recipe oldRecipe = recipeService.findById(recipeData.getId());
        if (!oldRecipe.getCreator().getId().equals(userId)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
        Recipe updatedRecipe = recipeService.updateRecipe(recipeData);
        if (updatedRecipe == null) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ApiResponse(updatedRecipe);
    }

    @DeleteMapping(RECIPE_API + VAR_RECIPE_ID)
    public ApiResponse deleteRecipe(HttpSession session, @PathVariable(RECIPE_ID) Long recipeId) {
        Recipe toDelete = recipeService.findById(recipeId);
        if (toDelete == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        Long userId = Utils.getLoggedInUser(session);
        if (userId == null || !userId.equals(toDelete.getCreator().getId())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
        recipeService.deleteRecipe(recipeId);
        return new ApiResponse(String.format("Recipe %d deleted", recipeId));
    }
}
