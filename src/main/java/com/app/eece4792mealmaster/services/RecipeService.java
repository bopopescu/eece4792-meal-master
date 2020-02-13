package com.app.eece4792mealmaster.services;

import com.app.eece4792mealmaster.dto.RecipeDto;
import com.app.eece4792mealmaster.dto.RecipeIngredientDto;
import com.app.eece4792mealmaster.models.GenericFood;
import com.app.eece4792mealmaster.models.Recipe;
import com.app.eece4792mealmaster.models.RecipeIngredient;
import com.app.eece4792mealmaster.models.User;
import com.app.eece4792mealmaster.repositories.GenericFoodRepository;
import com.app.eece4792mealmaster.repositories.RecipeRepository;
import com.app.eece4792mealmaster.repositories.UserRepository;
import com.app.eece4792mealmaster.utils.Utils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Transactional
@Service
public class RecipeService {
    @Autowired
    private RecipeRepository recipeRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private GenericFoodRepository genericFoodRepository;

    @Autowired
    private ModelMapper modelMapper;

    public Collection<RecipeDto> searchRecipes(String searchTerms) {
        return searchTerms.equals("") ? new ArrayList<>() : convertToDtoCollection(recipeRepository.searchRecipes(searchTerms));
    }

    public Collection<RecipeDto> getRecipeByUser(Long userId) {
        Optional<User> oUser = userRepository.findById(userId);
        return convertToDtoCollection(oUser.<Collection<Recipe>>map(User::getCreatedRecipes).orElse(new ArrayList<>()));
    }

    public RecipeDto createRecipe(Long userId, RecipeDto recipeDto) throws ParseException {
        Optional<User> oUser = userRepository.findById(userId);
        if (!oUser.isPresent()) {
            return null;
        }
        Recipe recipe = convertToEntity(recipeDto);
        recipe.setCreator(oUser.get());
        recipeRepository.save(recipe);
        return convertToDto(recipe);
    }

    public RecipeDto updateRecipe(RecipeDto recipeData) {
        Recipe newRecipe = this.findById(recipeData.getId());
        if (newRecipe == null) return null;
        Utils.updateModel(newRecipe, recipeData);
        recipeRepository.save(newRecipe);
        return convertToDto(newRecipe);
    }

    public Recipe findById(Long recipeId) {
        return recipeRepository.findById(recipeId).orElse(null);
    }

    public boolean deleteRecipe(Long recipeId) {
        Optional<Recipe> toDelete = recipeRepository.findById(recipeId);
        toDelete.ifPresent(recipe -> recipeRepository.delete(recipe));
        return toDelete.isPresent();
    }

    private Collection<RecipeDto> convertToDtoCollection(Collection<Recipe> recipes) {
        ArrayList<RecipeDto> ans = new ArrayList<>();
        for (Recipe recipe : recipes) {
            ans.add(convertToDto(recipe, false));
        }
        return ans;
    }

    public RecipeDto convertToDto(Recipe recipe) {
        return convertToDto(recipe, true);
    }

    public RecipeDto convertToDto(Recipe recipe, boolean fetchRecipes) {
        if (recipe == null) return null;
        modelMapper.typeMap(Recipe.class, RecipeDto.class).addMappings(mapper -> mapper.skip(RecipeDto::setIngredients));
        RecipeDto recipeDto = modelMapper.map(recipe, RecipeDto.class);
        recipeDto.setCreatorId(recipe.getCreator().getId());
        recipeDto.setFormattedCreateDate(recipe.getCreateDate());
        if (fetchRecipes) {
            List<RecipeIngredientDto> ingredientsDto = new ArrayList<>();
            for (RecipeIngredient ingredient : recipe.getRecipeIngredients()) {
                RecipeIngredientDto ingredientDto = new RecipeIngredientDto();
                ingredientDto.setIngredient(ingredient.getIngredient().getId());
                ingredientDto.setServings(ingredient.getServings());
                ingredientsDto.add(ingredientDto);
            }
            recipeDto.setIngredients(ingredientsDto);
        }
        return recipeDto;
    }

    public Recipe convertToEntity(RecipeDto recipeDto) throws ParseException {
        if (recipeDto == null) return null;
        Recipe recipe = modelMapper.map(recipeDto, Recipe.class);
        recipe.setCreateDate(recipeDto.getCreateDate());
        Optional<User> oCreator = userRepository.findById(recipeDto.getCreatorId());
        if (!oCreator.isPresent()) return null;
        recipe.setCreator(oCreator.get());
        for (RecipeIngredientDto ingredientDto : recipeDto.getIngredients()) {
            if (!(ingredientDto.getIngredient() == null || ingredientDto.getServings() == null)) {
                Optional<GenericFood> oIngredient = genericFoodRepository.findById(ingredientDto.getIngredient());
                oIngredient.ifPresent(genericFood -> recipe.addIngredient(genericFood, ingredientDto.getServings()));
            }
        }
        return recipe;
    }
}
