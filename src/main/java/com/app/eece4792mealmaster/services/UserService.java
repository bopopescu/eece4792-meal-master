package com.app.eece4792mealmaster.services;

import com.app.eece4792mealmaster.models.User;
import com.app.eece4792mealmaster.repositories.UserRepository;
import com.app.eece4792mealmaster.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

@Transactional
@Service
public class UserService {
  @Autowired
  private UserRepository userRepository;

  public User authenticatedUser(String usernameEmail, String password) {
    return userRepository.findUserByCredentials(usernameEmail, password);
  }

  public boolean createProfile(User user) {
    if (userRepository.findUserByUsername(user.getUsername()) != null || userRepository.findUserByEmail(user.getEmail()) != null) {
      return false;
    }
    userRepository.save(user);
    return true;
  }

  public boolean deleteProfile(Long userId) {
    Optional<User> profile = userRepository.findById(userId);
    profile.ifPresent(user -> userRepository.delete(user));
    return profile.isPresent();
  }

  public User updateProfile(Long userId, User userData) {
    Optional<User> profile = userRepository.findById(userId);
    if (profile.isPresent()) {
      User updatedProfile = profile.get();
      Utils.updateModel(updatedProfile, userData);
      return updatedProfile;
    } else {
      return null;
    }
  }

  public Collection<User> searchUsers(String searchTerms) {
    Collection<User> results;
    if (searchTerms == null || searchTerms.equals("")) {
      results = new ArrayList<>();
    } else {
      results = userRepository.searchUsers(searchTerms);
    }
    return results;
  }

  public User findById(Long userId) {
    Optional<User> oUser = userRepository.findById(userId);
    return oUser.orElse(null);
  }
}
