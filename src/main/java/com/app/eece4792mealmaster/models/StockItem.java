package com.app.eece4792mealmaster.models;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.Date;
import java.util.Objects;
import javax.persistence.*;

@Entity
@Table(name = "stockItem")
public class StockItem {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @JsonIgnore
  @ManyToOne
  @JoinColumn(name = "food_stock_id")
  private FoodStock foodStock;

  private String location;

  private Date dateObtained;

  private Date expirationDate;

  private Double quantity;

  // Getters and setters

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public FoodStock getFoodStock() {
    return foodStock;
  }

  public void setFoodStock(FoodStock foodStock) {
    this.foodStock = foodStock;
  }

  public String getLocation() {
    return location;
  }

  public void setLocation(String location) {
    this.location = location;
  }

  public Date getDateObtained() {
    return dateObtained;
  }

  public void setDateObtained(Date dateObtained) {
    this.dateObtained = dateObtained;
  }

  public Date getExpirationDate() {
    return expirationDate;
  }

  public void setExpirationDate(Date expirationDate) {
    this.expirationDate = expirationDate;
  }

  public Double getQuantity() {
    return quantity;
  }

  public void setQuantity(Double quantity) {
    this.quantity = quantity;
  }

  @Transient
  public Double getQuantityInGrams() {
    return this.foodStock.getFood().getGramsPerServing() * this.quantity;
  }

  /**
   * @return whether this food stock item is expired
   */
//  @Transient
//  public boolean isExpired() {
//    return LocalDate.now(ZoneId.systemDefault()).isAfter(expirationDate);
//  }

  @Override
  public boolean equals(Object o) {
    if (o instanceof StockItem) {
      StockItem givenStockItem = (StockItem) o;
      return this.id.equals(givenStockItem.id);
    }
    return false;
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.id,
        this.expirationDate,
        this.dateObtained,
        this.location,
        this.quantity,
        this.foodStock);
  }

}
