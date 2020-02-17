package com.app.eece4792mealmaster.dto;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class RecipeIngredientId implements Serializable {
    @Column(name = "recipe_id")
    private Long recipeId;

    @Column(name = "generic_food_id")
    private Long genericFoodId;

    public RecipeIngredientId() {}

    public RecipeIngredientId(Long recipeId, Long genericFoodId) {
        this.recipeId = recipeId;
        this.genericFoodId = genericFoodId;
    }

    public Long getRecipeId() {
        return recipeId;
    }

    public Long getGenericFoodId() {
        return genericFoodId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RecipeIngredientId that = (RecipeIngredientId) o;
        return getRecipeId().equals(that.getRecipeId()) &&
                getGenericFoodId().equals(that.getGenericFoodId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getRecipeId(), getGenericFoodId());
    }
}