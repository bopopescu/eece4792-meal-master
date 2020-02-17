package com.app.eece4792mealmaster.models;

import com.app.eece4792mealmaster.models.products.Product;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "genericFoods")
public class GenericFood {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String name;

  private int expirationInDays;

  private double gramsPerServing;

  @JsonIgnore
  @OneToMany (mappedBy = "genericClassification")
  private Set<Product> members;

  @JsonIgnore
  private Set<FoodGroup> foodGroups;

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

  public int getExpirationInDays() {
    return expirationInDays;
  }

  public void setExpirationInDays(int expirationInDays) {
    this.expirationInDays = expirationInDays;
  }

  public double getGramsPerServing() {
    return gramsPerServing;
  }

  public void setGramsPerServing(double gramsPerServing) {
    this.gramsPerServing = gramsPerServing;
  }

  public Set<Product> getMembers() {
    return members;
  }

  public void setMembers(Set<Product> members) {
    this.members = members;
  }

  public void setFoodGroups(Set<FoodGroup> foodGroups) {
    this.foodGroups = foodGroups;
  }

  public Set<FoodGroup> getFoodGroups() {
    return foodGroups;
  }

  public Set<FoodStock> getStocks() {
    return stocks;
  }

  public void setStocks(Set<FoodStock> stocks) {
    this.stocks = stocks;
  }
}
