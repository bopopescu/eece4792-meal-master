package com.app.eece4792mealmaster.models;

import com.app.eece4792mealmaster.dto.RecipeDto;
import com.app.eece4792mealmaster.utils.Views;
import com.fasterxml.jackson.annotation.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.util.*;

@Entity
@Table(name = "recipes")
public class Recipe {
    public Recipe() {
    }

    public Recipe(RecipeDto recipeDto)
    {
        id = recipeDto.getId();
        descriptions = recipeDto.getDescriptions();
        instructions = recipeDto.getInstructions();
        name = recipeDto.getName();
        yield = recipeDto.getYield();
        cookTime = recipeDto.getCookTime();
        tags = recipeDto.getTags();
    }

    public Recipe(Recipe recipe)
    {
        id = recipe.getId();
        creator = recipe.getCreator();
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "creator_id")
    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
    @JsonIdentityReference(alwaysAsId = true)
    private User creator;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "create_date")
    private Date createDate;

    @JsonView(Views.Detailed.class)
    @OneToMany(
            mappedBy = "recipe",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<RecipeIngredient> recipeIngredients = new ArrayList<>();

    @JsonView(Views.Detailed.class)
    @Column(columnDefinition = "TEXT")
    private String instructions;

    @Column(nullable = false)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String descriptions;

    private Integer yield;

    private Integer cookTime;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "recipe_tag",
            joinColumns = @JoinColumn(name = "recipe_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    private Set<Tag> tags = new HashSet<>();

    @JsonIgnore
    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "savedRecipes")
    private Set<User> savedByUsers = new HashSet<>();

    public void addIngredient(GenericFood ingredient, Double servings) {
        RecipeIngredient recipeIngredient = new RecipeIngredient(this, ingredient);
        recipeIngredient.setServings(servings);
        this.recipeIngredients.add(recipeIngredient);
    }

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getCreator() {
        return creator;
    }

    public void setCreator(User creator) {
        this.creator = creator;
    }

    public List<RecipeIngredient> getRecipeIngredients() {
        return recipeIngredients;
    }

    public void setRecipeIngredients(List<RecipeIngredient> recipeIngredients) {
        this.recipeIngredients.clear();
        this.recipeIngredients.addAll(recipeIngredients);
    }

    public String getInstructions() {
        return instructions;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescriptions() {
        return descriptions;
    }

    public void setDescriptions(String descriptions) {
        this.descriptions = descriptions;
    }

    public Integer getYield() {
        return yield;
    }

    public void setYield(Integer yield) {
        this.yield = yield;
    }

    public Set<Tag> getTags() {
        return tags;
    }

    public Set<User> getSavedByUsers() {
        return savedByUsers;
    }

    public void setSavedByUsers(Set<User> savedByUsers) {
        this.savedByUsers = savedByUsers;
    }

    public void setTags(Set<Tag> tags) {
        this.tags = tags;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Integer getCookTime() {
        return cookTime;
    }

    public void setCookTime(Integer cookTime) {
        this.cookTime = cookTime;
    }
}
