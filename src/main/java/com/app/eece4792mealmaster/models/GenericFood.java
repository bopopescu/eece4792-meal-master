package com.app.eece4792mealmaster.models;

import com.app.eece4792mealmaster.constants.Routes;
import com.app.eece4792mealmaster.models.products.Product;
import com.app.eece4792mealmaster.utils.Utils;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.*;

@Entity
@Table(name = "genericFoods")
public class GenericFood {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String name;

  private Integer daysGood;
  private String storedIn;
  private Integer calories;
  private String carbs;
  private String protein;
  private String totalFat;
  private Double servingSize;
  private String servingSizeUnit;

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

  @Transient
  public String getImage() {
    return Utils.imageUrlBuilder(id, Routes.FOOD);
  }

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

  // Outdated remove
  public Integer getAverageExpirationDurations() {
    return daysGood;
  }
  // Outdated remove
  public void setAverageExpirationDurations(Integer daysGood) {
    this.daysGood = daysGood;
  }

  public Set<Tag> getTags() {
    return tags;
  }

  public void setTags(Set<Tag> tags) {
    this.tags = tags;
  }
  // Outdated remove
  public Double getGramsPerServing() {
    return servingSize;
  }
  // Outdated remove
  public void setGramsPerServing(Double servingSize) {
    this.servingSize = servingSize;
  }

  public Set<Product> getMembers() {
    return members;
  }

  public void setMembers(Set<Product> members) {
    this.members = members;
  }
  
  public Integer getDaysGood() {
    return daysGood;
  }

  public void setDaysGood(Integer daysGood) {
    this.daysGood = daysGood;
  }

  public String getStoredIn() {
    return storedIn;
  }

  public void setStoredIn(String storedIn) {
    this.storedIn = storedIn;
  }
  
  public Integer getCalories() {
    return calories;
  }

  public void setCalories(Integer calories) {
    this.calories = calories;
  }

  public String getCarbs() {
    return carbs;
  }

  public void setCarbs(String carbs) {
    this.carbs = carbs;
  }

  public String getProtein() {
    return protein;
  }

  public void setProtein(String protein) {
    this.protein = protein;
  }

  public String getTotalFat() {
    return totalFat;
  }

  public void setTotalFat(String totalFat) {
    this.totalFat = totalFat;
  }

  public Double getServingSize() {
    return servingSize;
  }

  public void setServingSize(Double servingSize) {
    this.servingSize = servingSize;
  }
 
  public String getServingSizeUnit() {
    return servingSizeUnit;
  }

  public void setServingSizeUnit(String servingSizeUnit) {
    this.servingSizeUnit = servingSizeUnit;
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
