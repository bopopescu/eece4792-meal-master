package com.app.eece4792mealmaster.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.*;

@Entity
@Table(name = "users")
public class User {

  public User() {}

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @CreationTimestamp
  @Temporal(TemporalType.TIMESTAMP)
  @Column(name = "create_date")
  private Date createDate;

  private String firstName;
  private String lastName;
  @Column(unique=true, nullable=false)
  private String username;
  @Column(unique=true, nullable=false)
  private String email;
  private LocalDate dob;
  @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
  @Column(nullable=false)
  private String password;

  @JsonIgnore
  @ManyToMany(fetch = FetchType.LAZY, cascade = {
          CascadeType.PERSIST,
          CascadeType.MERGE
  })
  @JoinTable(name = "follows",
          joinColumns = @JoinColumn(name = "following_id"),
          inverseJoinColumns = @JoinColumn(name = "follower_id")
  )
  private Set<User> followers = new HashSet<>();

  @JsonIgnore
  @ManyToMany(fetch = FetchType.LAZY, mappedBy = "followers")
  private Set<User> following = new HashSet<>();

  @JsonIgnore
  @ManyToMany(fetch = FetchType.LAZY)
  @JoinTable(
          name = "user_saved_recipes",
          joinColumns = @JoinColumn(name = "user_id"),
          inverseJoinColumns = @JoinColumn(name = "recipe_id")
  )
  private Set<Recipe> savedRecipes = new HashSet<Recipe>();

  @JsonIgnore
  @OneToMany(fetch = FetchType.LAZY, mappedBy = "creator")
  private Set<Recipe> createdRecipes;

  @Transient
  public String getFullName() {
    return String.format("%s %s", this.firstName, this.lastName);
  }

  public void follow(User user) {
    this.following.add(user);
    user.getFollowers().add(this);
  }

  // Getters and Setters
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public Set<User> getFollowers() {
    return followers;
  }

  public void setFollowers(Set<User> followers) {
    this.followers = followers;
  }

  public Set<User> getFollowing() {
    return following;
  }

  public Set<Recipe> getCreatedRecipes() {
    return createdRecipes;
  }

  public void setCreatedRecipes(Set<Recipe> createdRecipes) {
    this.createdRecipes = createdRecipes;
  }

  public void setFollowing(Set<User> following) {
    this.following = following;
  }

  public LocalDate getDob() {
    return dob;
  }

  public void setDob(LocalDate dob) {
    this.dob = dob;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public Date getCreateDate() {
    return createDate;
  }

  public void setCreateDate(Date createDate) {
    this.createDate = createDate;
  }

  public Set<Recipe> getSavedRecipes() {
    return savedRecipes;
  }

  public void setSavedRecipes(Set<Recipe> savedRecipes) {
    this.savedRecipes = savedRecipes;
  }
}
