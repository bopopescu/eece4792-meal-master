package com.app.eece4792mealmaster.models;

import com.app.eece4792mealmaster.dto.RecipeIngredientId;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "recipe_ingredients")
public class RecipeIngredient {

    @EmbeddedId
    private RecipeIngredientId id;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("recipeId")
    private Recipe recipe;

    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
    @JsonIdentityReference(alwaysAsId = true)
    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("genericFoodId")
    private GenericFood ingredient;

    @Column(name = "servings")
    private Double servings;

    private RecipeIngredient() {}

    public RecipeIngredient(Recipe recipe, GenericFood ingredient) {
        this.recipe = recipe;
        this.ingredient = ingredient;
        this.id = new RecipeIngredientId(recipe.getId(), ingredient.getId());
    }

    public RecipeIngredientId getId() {
        return id;
    }

    public void setId(RecipeIngredientId id) {
        this.id = id;
    }

    public Recipe getRecipe() {
        return recipe;
    }

    public void setRecipe(Recipe recipe) {
        this.recipe = recipe;
    }

    public GenericFood getIngredient() {
        return ingredient;
    }

    public void setIngredient(GenericFood ingredient) {
        this.ingredient = ingredient;
    }

    public Double getServings() {
        return servings;
    }

    public void setServings(Double servings) {
        this.servings = servings;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RecipeIngredient that = (RecipeIngredient) o;
        return getRecipe().equals(that.getRecipe()) &&
                getIngredient().equals(that.getIngredient());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getRecipe(), getIngredient());
    }
}
