package com.app.eece4792mealmaster.services;

import com.app.eece4792mealmaster.constants.Consts;
import com.app.eece4792mealmaster.models.Recipe;
import com.app.eece4792mealmaster.models.User;
import com.app.eece4792mealmaster.repositories.RecipeRepository;
import com.app.eece4792mealmaster.repositories.UserRepository;
import com.app.eece4792mealmaster.utils.ApiResponse;
import com.app.eece4792mealmaster.utils.Utils;
import com.app.eece4792mealmaster.utils.exceptions.BadRequest;
import com.app.eece4792mealmaster.utils.exceptions.ForbiddenRequestException;
import com.app.eece4792mealmaster.utils.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
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

    public ApiResponse searchRecipes(String searchTerms) {
        Collection<Recipe> responseBody = searchTerms.equals("") ? new ArrayList<>() : recipeRepository.searchRecipes(searchTerms);
        return new ApiResponse(responseBody);
    }

    public ApiResponse getRecipeByUser(Long userId) {
        if (userId == null) {
            throw new BadRequest();
        }
        Optional<User> oUser = userRepository.findById(userId);
        if (!oUser.isPresent()) {
            throw new ResourceNotFoundException();
        }
        return new ApiResponse(oUser.get().getCreatedRecipes());
    }

    public ApiResponse createRecipe(HttpSession session, Recipe recipe) {
        Long userId = (Long)(session.getAttribute(Consts.SessionConsts.USER_ID));
        if (userId == null || recipe == null) {
            throw new BadRequest();
        }
        Optional<User> oUser = userRepository.findById(userId);
        if (!oUser.isPresent()) {
            throw new ResourceNotFoundException();
        }
        recipe.setCreator(oUser.get());
        recipeRepository.save(recipe);
        return new ApiResponse(recipe);
    }

    public ApiResponse updateRecipe(HttpSession session, Recipe recipeData) {
        Long userId = (Long)(session.getAttribute(Consts.SessionConsts.USER_ID));
        if (userId == null || recipeData == null) { throw new BadRequest(); }
        Optional<Recipe> oRecipe = recipeRepository.findById(recipeData.getId());
        if (!oRecipe.isPresent()) {
            throw new ResourceNotFoundException();
        }
        Recipe newRecipe = oRecipe.get();
        if (!newRecipe.getCreator().getId().equals(userId)) {
            throw new ForbiddenRequestException();
        }
        Utils.updateModel(newRecipe, recipeData);
        recipeRepository.save(newRecipe);
        return new ApiResponse(newRecipe);
    }

    public ApiResponse deleteRecipe(HttpSession session, Long recipeId) {
        Long userId = (Long)(session.getAttribute(Consts.SessionConsts.USER_ID));
        Optional<Recipe> oRecipe = recipeRepository.findById(recipeId);
        if (userId == null || recipeId == null || !oRecipe.isPresent()) {
            throw new ResourceNotFoundException();
        }
        if (!oRecipe.get().getCreator().getId().equals(userId)) {
            throw new ForbiddenRequestException();
        }
        recipeRepository.deleteById(recipeId);
        return new ApiResponse();
    }
}
