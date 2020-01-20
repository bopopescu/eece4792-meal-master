package com.app.eece4792mealmaster.models;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "tags")
public class Tag {

    public Tag() {}

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique=true, nullable=false)
    private String name;

    @JsonIgnore
    @ManyToMany(mappedBy = "tags")
    private Set<Recipe> taggedRecipes;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Recipe> getTaggedRecipes() {
        return taggedRecipes;
    }

    public void setTaggedRecipes(Set<Recipe> taggedRecipes) {
        this.taggedRecipes = taggedRecipes;
    }
}
