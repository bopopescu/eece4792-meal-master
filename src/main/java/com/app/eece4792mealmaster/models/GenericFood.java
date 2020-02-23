package com.app.eece4792mealmaster.models;

import com.app.eece4792mealmaster.models.products.Product;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "genericFoods")
public class GenericFood {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String name;

  private Integer averageExpirationDurations;

  private Double gramsPerServing;

  @ManyToMany(fetch = FetchType.EAGER)
  @JoinTable(
      name = "generic_food_tag",

      joinColumns = @JoinColumn(name = "generic_food_id"),
      inverseJoinColumns = @JoinColumn(name = "tag_id")
  )
  private Set<Tag> tags = new HashSet<>();

  @JsonIgnore
  @OneToMany (mappedBy = "genericClassification")
  private Set<Product> members;

//  @JsonIgnore
//  private Set<FoodGroup> foodGroups;

  @JsonIgnore
  @OneToMany (mappedBy = "food")
  private Set<FoodStock> stocks;

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

  public Integer getAverageExpirationDurations() {
    return averageExpirationDurations;
  }

  public void setAverageExpirationDurations(Integer averageExpirationDurations) {
    this.averageExpirationDurations = averageExpirationDurations;
  }

  public Set<Tag> getTags() {
    return tags;
  }

  public void setTags(Set<Tag> tags) {
    this.tags = tags;
  }

  public Double getGramsPerServing() {
    return gramsPerServing;
  }

  public void setGramsPerServing(Double gramsPerServing) {
    this.gramsPerServing = gramsPerServing;
  }

  public Set<Product> getMembers() {
    return members;
  }

  public void setMembers(Set<Product> members) {
    this.members = members;
  }
//
//  public void setFoodGroups(Set<FoodGroup> foodGroups) {
//    this.foodGroups = foodGroups;
//  }
//
//  public Set<FoodGroup> getFoodGroups() {
//    return foodGroups;
//  }

  public Set<FoodStock> getStocks() {
    return stocks;
  }

  public void setStocks(Set<FoodStock> stocks) {
    this.stocks = stocks;
  }
}
