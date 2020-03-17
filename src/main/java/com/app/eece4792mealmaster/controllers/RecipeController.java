package com.app.eece4792mealmaster.controllers;

import com.app.eece4792mealmaster.dto.RecipeDto;
import com.app.eece4792mealmaster.services.RecipeService;
import com.app.eece4792mealmaster.utils.ApiResponse;
import com.app.eece4792mealmaster.utils.Utils;
import com.app.eece4792mealmaster.utils.Views;
import com.fasterxml.jackson.annotation.JsonView;
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
}
