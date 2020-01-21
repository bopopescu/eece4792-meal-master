package com.app.eece4792mealmaster.models;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import java.util.stream.Collectors;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "foodStock")
public class FoodStock {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @JsonIgnore
  @OneToMany (mappedBy = "stockItems")
  private Set<StockItem> stockItems;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Set<StockItem> getStockItems() {
    return stockItems;
  }

  public void setStockItems(Set<StockItem> stockItems) {
    this.stockItems = stockItems;
  }

  /**
   * Retrieves the quantity of the desired stock item in this food stock
   * @param stockItem the desired stock item
   * @return Optional of the quantity for the desired stock item
   */
  public Optional<Quantity> getTotalQuantity(StockItem stockItem) {
    List<StockItem> desiredItemList = stockItems.stream()
        .filter(si -> si.equals(stockItem))
        .collect(Collectors.toList());
    return desiredItemList.isEmpty() ?
        Optional.empty() :
        Optional.of(desiredItemList.get(0).getQuantity());
  }

  @Override
  public boolean equals(Object o) {
    if (o instanceof FoodStock) {
      FoodStock givenFoodStock = (FoodStock) o;
      return this.id.equals(givenFoodStock.id)
          && this.stockItems.containsAll(givenFoodStock.stockItems)
          && givenFoodStock.stockItems.containsAll(this.stockItems);
    }
    return false;
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.id);
  }

}
