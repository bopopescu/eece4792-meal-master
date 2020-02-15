package com.app.eece4792mealmaster.dto;

import com.app.eece4792mealmaster.constants.Consts;
import com.app.eece4792mealmaster.models.Tag;
import com.app.eece4792mealmaster.utils.Views;
import com.fasterxml.jackson.annotation.JsonView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class RecipeDto {
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat(Consts.SIMPLE_DATE_FORMAT);

    public RecipeDto() {}

    private Long id;

    private Long creatorId;

    private String formattedCreateDate;

    @JsonView(Views.Detailed.class)
    private String instructions;

    private String name;

    private String descriptions;

    private Integer yield;

    private Integer cookTime;

    private Set<Tag> tags = new HashSet<>();

    @JsonView(Views.Detailed.class)
    private List<RecipeIngredientDto> ingredients = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(Long creatorId) {
        this.creatorId = creatorId;
    }

    public String getFormattedCreateDate() {
        return this.formattedCreateDate;
    }

    public Date getCreateDate() throws ParseException {
        return dateFormat.parse(this.formattedCreateDate);
    }

    public void setFormattedCreateDate(Date createDate) {
        this.formattedCreateDate = dateFormat.format(createDate);
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

    public void setTags(Set<Tag> tags) {
        this.tags = tags;
    }

    public List<RecipeIngredientDto> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<RecipeIngredientDto> ingredients) {
        this.ingredients = ingredients;
    }

    public Integer getCookTime() {
        return cookTime;
    }

    public void setCookTime(Integer cookTime) {
        this.cookTime = cookTime;
    }
}
