package com.app.eece4792mealmaster.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "food_group")
public enum FoodGroup {

  FRUITS, VEGETABLES, GRAINS, PROTEIN, DAIRY;
}
