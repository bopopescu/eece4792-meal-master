package com.app.eece4792mealmaster.models;

import com.app.eece4792mealmaster.services.GenericFoodService;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.Objects;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import org.springframework.beans.factory.annotation.Autowired;

@Entity
@Table(name = "food_stock")
public class FoodStock {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String name;

  private double numberOfServingsNeeded;

  @ManyToOne
  @MapsId("userId")
  private long userId;

  @ManyToOne
  @MapsId("genericFoodId")
  private long genericFoodId;

  @JsonIgnore
  @OneToMany (mappedBy = "foodStock")
  private Set<StockItem> stockItems;

  @JsonIgnore
  @Autowired
  private GenericFoodService genericFoodService;

  @ManyToOne
  @MapsId("foodId")
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

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public double getNumberOfServingsNeeded() {
    return numberOfServingsNeeded;
  }

  public void setNumberOfServingsNeeded(double numberOfServingsNeeded) {
    this.numberOfServingsNeeded = numberOfServingsNeeded;
  }

  public long getUserId() {
    return userId;
  }

  public void setUserId(long userId) {
    this.userId = userId;
  }


  public long getGenericFoodId() {
    return genericFoodId;
  }

  public void setGenericFoodId(long genericFoodId) {
    this.genericFoodId = genericFoodId;
  }

  public Set<StockItem> getStockItems() {
    return stockItems;
  }

  public void setStockItems(Set<StockItem> stockItems) {
    this.stockItems = stockItems;
  }


  /**
   * Retrieves the quantity in grams that is required for this foodstock
   */
  @Transient
  public double getQuantityInGrams() {
    GenericFood genericFood = genericFoodService.getGenericFoodById(this.genericFoodId);
    return this.numberOfServingsNeeded * genericFood.getGramsPerServing();
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
