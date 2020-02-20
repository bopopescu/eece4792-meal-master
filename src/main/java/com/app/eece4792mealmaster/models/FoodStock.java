package com.app.eece4792mealmaster.models;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import java.util.*;
import javax.persistence.*;

@Entity
@Table(name = "food_stock")
public class FoodStock {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @JsonIgnore
  @ManyToOne
  @JoinColumn(name = "user_id")
  private User user;

  @OneToMany (mappedBy = "foodStock")
  private Set<StockItem> stockItems = new HashSet<>();

  @ManyToOne
  @JoinColumn(name = "food_id")
  @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
  @JsonIdentityReference(alwaysAsId = true)
  private GenericFood food;

  public GenericFood getFood() {
    return food;
  }

  public void setFood(GenericFood food) {
    this.food = food;
  }

  public void addStockItem(StockItem stockItem) {
    this.stockItems.add(stockItem);
  }

  public void removeStockItem(StockItem stockItem) {
    this.stockItems.remove(stockItem);
  }

  // Getters and setters

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public User getUser() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
  }

  public Set<StockItem> getStockItems() {
    return stockItems;
  }

  public void setStockItems(Set<StockItem> stockItems) {
    this.stockItems = stockItems;
  }

  /**
   * Retrieves the quantity of stock items for the given foodstock
   */
  @Transient
  public double getTotalQuantity() {
    Set<StockItem> desiredItemList = this.getStockItems();
    double totalQuantity = 0;
    for (StockItem stockItem : desiredItemList) {
      totalQuantity += stockItem.getQuantity();
    }

    return totalQuantity;
  }

  /**
   * Retrieves the quantity in grams that is required for this foodstock
   */
  @Transient
  public double getQuantityInGrams() {
    return this.getTotalQuantity() * this.food.getGramsPerServing();
  }

  @Override
  public boolean equals(Object o) {
    if (o instanceof FoodStock) {
      FoodStock givenFoodStock = (FoodStock) o;
      return this.id.equals(givenFoodStock.id);
    }
    return false;
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.id);
  }

}
