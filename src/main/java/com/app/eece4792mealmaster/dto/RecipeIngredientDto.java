package com.app.eece4792mealmaster.dto;

public class RecipeIngredientDto {
    private Long ingredient;

    private Double servings;

    public RecipeIngredientDto() {}

    public Long getIngredient() {
        return ingredient;
    }

    public void setIngredient(Long ingredient) {
        this.ingredient = ingredient;
    }

    public Double getServings() {
        return servings;
    }

    public void setServings(Double servings) {
        this.servings = servings;
    }
}
