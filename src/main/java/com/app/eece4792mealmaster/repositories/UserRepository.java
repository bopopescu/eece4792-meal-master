package com.app.eece4792mealmaster.repositories;

import com.app.eece4792mealmaster.models.User;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Collection;

public interface UserRepository extends CrudRepository<User, Long> {
  @Query("SELECT user " +
          "FROM User user " +
          "WHERE (user.username=:usernameEmail OR user.email=:usernameEmail) " +
          "AND user.password=:password")
  public User findUserByCredentials(@Param("usernameEmail") String usernameEmail,
                                    @Param("password") String password);

  @Query("SELECT user FROM User user WHERE user.username=:username")
  public User findUserByUsername(@Param("username") String username);

  @Query("SELECT user FROM User user WHERE user.email=:email")
  public User findUserByEmail(@Param("email") String email);

  @Query("SELECT user from User user WHERE user.username LIKE :searchTerms%")
  public Collection<User> searchUsers(@Param("searchTerms") String searchTerms);
}
