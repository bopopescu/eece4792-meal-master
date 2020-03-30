package com.app.eece4792mealmaster.services;

import com.app.eece4792mealmaster.models.FoodStock;
import com.app.eece4792mealmaster.dto.RecipeDto;
import com.app.eece4792mealmaster.dto.RecipeIngredientDto;
import com.app.eece4792mealmaster.models.GenericFood;
import com.app.eece4792mealmaster.models.Recipe;
import com.app.eece4792mealmaster.models.RecipeIngredient;
import com.app.eece4792mealmaster.models.User;
import com.app.eece4792mealmaster.repositories.FoodStockRepository;
import com.app.eece4792mealmaster.repositories.RecipeRepository;
import com.app.eece4792mealmaster.repositories.UserRepository;
import com.app.eece4792mealmaster.utils.Utils;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import com.app.eece4792mealmaster.repositories.GenericFoodRepository;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
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
    return searchTerms.equals("") ? new ArrayList<>() : convertToDtoCollection(recipeRepository.searchRecipes(searchTerms,
        PageRequest.of(0, 50)));
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
    recipeDto.setCreator(oUser.get().getId());
    Recipe recipe = convertToEntity(recipeDto);
    recipeRepository.save(recipe);
    return convertToDto(recipe);
  }

  public RecipeDto updateRecipe(RecipeDto recipeData) {
    Recipe recipeToUpdate = recipeRepository.findById(recipeData.getId()).orElse(null);
    if (recipeToUpdate == null) return null;
    try {
      Utils.updateModel(recipeToUpdate, convertToEntity(recipeData));
    }
    catch(ParseException e) { return null; } // do we want to return existing recipe or null?

    recipeRepository.save(recipeToUpdate);
    return convertToDto(recipeToUpdate);
  }

  public RecipeDto findById(Long recipeId) {
    return convertToDto(recipeRepository.findById(recipeId).orElse(null));
  }

  public boolean deleteRecipe(Long recipeId) {
    Optional<Recipe> toDelete = recipeRepository.findById(recipeId);
    toDelete.ifPresent(recipe -> recipeRepository.delete(recipe));
    return toDelete.isPresent();
  }

  public Collection<RecipeDto> getRecipeRecs(Long userId) {
    return convertToDtoCollection(recipeRepository.findRecipeRecs(userId, PageRequest.of(0, 6)));
  }

  public Collection<RecipeDto> getUserLikedRecipes(Long userId) {
    Optional<User> oUser = userRepository.findById(userId);
    return oUser.map(user -> convertToDtoCollection(user.getSavedRecipes())).orElse(null);
  }

  public Collection<RecipeDto> likeRecipe(Long userId, Long recipeId) {
    Optional<User> oUser = userRepository.findById(userId);
    Optional<Recipe> oRecipe = recipeRepository.findById(recipeId);
    if (!oUser.isPresent() || !oRecipe.isPresent()) {
      return null;
    }
    User user = oUser.get();
    user.saveRecipe(oRecipe.get());
    return convertToDtoCollection(user.getSavedRecipes());
  }

  public Collection<RecipeDto> unlikeRecipe(Long userId, Long recipeId) {
    Optional<User> oUser = userRepository.findById(userId);
    Optional<Recipe> oRecipe = recipeRepository.findById(recipeId);
    if (!oUser.isPresent() || !oRecipe.isPresent()) {
      return null;
    }
    User user = oUser.get();
    user.unsaveRecipe(oRecipe.get());
    return convertToDtoCollection(user.getSavedRecipes());
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
    // modelMapper.typeMap(Recipe.class, RecipeDto.class).addMappings(mapper -> mapper.skip(RecipeDto::setIngredients));
    // modelMapper.typeMap(Recipe.class, RecipeDto.class).addMapping(src -> src.getCreator().getId(), RecipeDto::setCreator);
    // RecipeDto recipeDto = modelMapper.map(recipe, RecipeDto.class);
    RecipeDto recipeDto = new RecipeDto(recipe);
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
    Optional<User> oCreator = userRepository.findById(recipeDto.getCreator());
    if (!oCreator.isPresent()) return null;
    // modelMapper.typeMap(RecipeDto.class, Recipe.class).addMappings(mapper -> mapper.skip(Recipe::setCreator));
    // Recipe recipe = modelMapper.map(recipeDto, Recipe.class);
    Recipe recipe = new Recipe(recipeDto);
    if (recipeDto.getFormattedCreateDate() != null) recipe.setCreateDate(recipeDto.getCreateDate());
    recipe.setCreator(oCreator.get());
    for (RecipeIngredientDto ingredientDto : recipeDto.getIngredients()) {
      if (!(ingredientDto.getIngredient() == null || ingredientDto.getServings() == null)) {
        Optional<GenericFood> oIngredient = genericFoodRepository.findById(ingredientDto.getIngredient());
        oIngredient.ifPresent(genericFood -> recipe.addIngredient(genericFood, ingredientDto.getServings()));
      }
    }
    return recipe;
  }

  /**
   * Determines if this recipe this can be made
   */
  public boolean canRecipeBeMade(Recipe recipe, User user) {
    Map<GenericFood, Double> ingredientsAndServings = recipe.getRecipeIngredients().stream()
            .collect(Collectors.toMap(
                    RecipeIngredient::getIngredient,
                    RecipeIngredient::getServings)
            );

    Collection<FoodStock> foodStocks = user.getFoodStocks();
    Map<GenericFood, FoodStock> foodStockByGenericFoodId = foodStocks.stream().collect(
            Collectors.toMap(FoodStock::getFood, Function.identity()));

    for(Map.Entry<GenericFood, Double> entry : ingredientsAndServings.entrySet()) {
      GenericFood genericFood = entry.getKey();
      if (!foodStockByGenericFoodId.containsKey(genericFood)) {
        return false;
      }
      double availableServings = foodStockByGenericFoodId.get(genericFood).getTotalQuantity();
      // if the amount that we need is more than what we have, return false
      if (ingredientsAndServings.get(genericFood) > availableServings) {
        return false;
      }
    }
    return true;
  }
}
