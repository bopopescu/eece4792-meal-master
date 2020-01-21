package com.app.eece4792mealmaster.models;

import java.util.Objects;

/**
 * Represents a quantity of something (in particular, a quantity of a stock item)
 */
public class Quantity {

  private double amount;
  private String units;

  public Quantity(double amount, String units) {
    this.amount = amount;
    this.units = units;
  }

  public Quantity(double amount) {
    this(amount, "");
  }

  public double getAmount() {
    return amount;
  }

  public void setAmount(double amount) {
    this.amount = amount;
  }

  public String getUnits() {
    return units;
  }

  public void setUnits(String units) {
    this.units = units;
  }

  @Override
  public boolean equals(Object o) {
    if (o instanceof Quantity) {
      Quantity givenQuantity = (Quantity) o;
      return this.amount == (givenQuantity.amount)
          && this.units.equals(givenQuantity.units);
    }
    return false;
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.amount, this.units);
  }

}
