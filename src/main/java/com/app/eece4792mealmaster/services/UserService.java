package com.app.eece4792mealmaster.services;

import com.app.eece4792mealmaster.constants.Consts;
import com.app.eece4792mealmaster.models.User;
import com.app.eece4792mealmaster.repositories.UserRepository;
import com.app.eece4792mealmaster.utils.ApiResponse;
import com.app.eece4792mealmaster.utils.Utils;
import com.app.eece4792mealmaster.utils.exceptions.BadRequest;
import com.app.eece4792mealmaster.utils.exceptions.ResourceExistsException;
import com.app.eece4792mealmaster.utils.exceptions.ResourceNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

@Transactional
@Service
public class UserService {
  @Autowired
  private UserRepository userRepository;

  public ApiResponse login(HttpSession session, String usernameEmail, String password) {
    User user = userRepository.findUserByCredentials(usernameEmail, password);
    if (user == null) {
      throw new ResourceNotFoundException();
    }
    session.setAttribute(Consts.SessionConsts.USER_ID, user.getId());
    return new ApiResponse(200, "Login successful", user);
  }

  public ApiResponse logout(HttpSession session) {
    Long userId = Utils.getLoggedInUser(session);
    if (userId == null) { throw new BadRequest(); }
    session.invalidate();
    return new ApiResponse(200, "Logout successful", null);
  }

  public ApiResponse register(User user) {
    if (user.getUsername() == null || user.getEmail() == null || user.getPassword() == null) {
      throw new BadRequest();
    }
    if (userRepository.findUserByUsername(user.getUsername()) != null) {
      throw new ResourceExistsException();
    }
    if (userRepository.findUserByEmail(user.getEmail()) != null) {
      throw new ResourceExistsException();
    }
    userRepository.save(user);
    return new ApiResponse(200, "Registration Successful", null);
  }

  public ApiResponse profile(HttpSession session) {
    Long userId = Utils.getLoggedInUser(session);
    if (userId == null) { throw new BadRequest(); }
    Optional<User> profile = userRepository.findById(userId);
    if (profile.isPresent()) {
      return new ApiResponse(profile.get());
    } else {
      throw new ResourceNotFoundException();
    }
  }

  public ApiResponse deleteProfile(HttpSession session) {
    Long userId = Utils.getLoggedInUser(session);
    System.out.println(userId);
    if (userId == null) { throw new BadRequest(); }
    Optional<User> profile = userRepository.findById(userId);
    if (profile.isPresent()) {
      userRepository.delete(profile.get());
      session.invalidate();
      return new ApiResponse(String.format("User %s successfully deleted", userId));
    } else {
      throw new ResourceNotFoundException();
    }
  }

  public ApiResponse updateProfile(HttpSession session, User userData) {
    Long userId = Utils.getLoggedInUser(session);
    if (userId == null) { throw new BadRequest(); }
    Optional<User> profile = userRepository.findById(userId);
    if (profile.isPresent()) {
      User updatedProfile = profile.get();
      Utils.updateModel(updatedProfile, userData);
      return new ApiResponse(updatedProfile);
    } else {
      throw new ResourceNotFoundException();
    }
  }

  public ApiResponse searchUsers(String searchTerms) {
    Collection<User> results;
    if (searchTerms == null || searchTerms.equals("")) {
      results = new ArrayList<>();
    } else {
      results = userRepository.searchUsers(searchTerms);
    }
    return new ApiResponse(results);
  }
}
