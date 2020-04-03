package com.app.eece4792mealmaster.models;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.Date;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

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

  @OneToMany(mappedBy = "foodStock", cascade = CascadeType.REMOVE, orphanRemoval = true)
  private Set<StockItem> stockItems = new HashSet<>();

  @ManyToOne
  @JoinColumn(name = "food_id")
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

  @JsonIgnore
  public Long getFoodId() {
    return this.food.getId();
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

  @Transient
  public String getFoodName() {
    return this.getFood().getName();
  }

  /**
   * Retrieves the quantity in grams that is required for this foodstock
   */
  @Transient
  public double getQuantityInGrams() {
    return this.getTotalQuantity() * this.food.getGramsPerServing();
  }

  @Transient
  public Date getNextExpiration() {
    Date nextExpiration = null;
    for (StockItem stockItem : stockItems) {
      nextExpiration = nextExpiration == null
              || nextExpiration.compareTo(stockItem.getExpirationDate()) > 0
              ? stockItem.getExpirationDate() : nextExpiration;
    }
    return nextExpiration;
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
