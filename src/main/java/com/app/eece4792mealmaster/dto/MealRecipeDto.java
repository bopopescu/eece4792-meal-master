package com.app.eece4792mealmaster.dto;

public class MealRecipeDto {
    private Long id;

    private Double servings;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getServings() {
        return servings;
    }

    public void setServings(Double servings) {
        this.servings = servings;
    }
}
