package com.app.eece4792mealmaster.models;

import com.app.eece4792mealmaster.models.products.Product;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "genericFoods")
public class GenericFood {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @JsonIgnore
  @OneToMany (mappedBy = "genericClassification")
  private Set<Product> members;

  @JsonIgnore
  @OneToOne (mappedBy = "foodStockName")
  private FoodStock foodStock;

  @JsonIgnore
  private Set<FoodGroup> foodGroups;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Set<Product> getMembers() {
    return members;
  }

  public void setMembers(Set<Product> members) {
    this.members = members;
  }

  public FoodStock getFoodStock() {
    return foodStock;
  }

  public void setFoodStock(FoodStock foodStock) {
    this.foodStock = foodStock;
  }

  public Set<FoodGroup> getFoodGroups() {
    return foodGroups;
  }

  public void setFoodGroups(Set<FoodGroup> foodGroups) {
    this.foodGroups = foodGroups;
  }
}
