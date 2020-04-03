package com.app.eece4792mealmaster.dto;

import java.util.HashSet;
import java.util.Set;

public class MealDto {
    private Set<MealFoodDto> foods = new HashSet<>();

    private Set<MealRecipeDto> recipes = new HashSet<>();

    public Set<MealFoodDto> getFoods() {
        return foods;
    }

    public void setFoods(Set<MealFoodDto> foods) {
        this.foods = foods;
    }

    public Set<MealRecipeDto> getRecipes() {
        return recipes;
    }

    public void setRecipes(Set<MealRecipeDto> recipes) {
        this.recipes = recipes;
    }
}
