package com.app.eece4792mealmaster.models;

import com.app.eece4792mealmaster.models.products.Product;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.util.*;

@Entity
@Table(name = "recipes")
public class Recipe {
    public Recipe() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @MapsId("userId")
    private User creator;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "create_date")
    private Date createDate;

    @ElementCollection
    @CollectionTable(name = "recipe_ingredients", joinColumns = @JoinColumn(name = "recipe_id"))
    @MapKeyJoinColumn(name = "ndbNo")
    @Column(name = "servings")
    private Map<Product, Double> ingredients = new HashMap<>();

    @Column(columnDefinition = "TEXT")
    private String instructions;
    @Column(nullable = false)
    private String name;
    @Column(columnDefinition = "TEXT")
    private String descriptions;
    private Integer yield;

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

    public Map<Product, Double> getIngredients() {
        return ingredients;
    }

    public void setIngredients(Map<Product, Double> ingredients) {
        this.ingredients = ingredients;
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
}
