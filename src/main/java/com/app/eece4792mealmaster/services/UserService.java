package com.app.eece4792mealmaster.services;

import com.app.eece4792mealmaster.constants.Consts;
import com.app.eece4792mealmaster.models.User;
import com.app.eece4792mealmaster.repositories.UserRepository;
import com.app.eece4792mealmaster.utils.ApiResponse;
import com.app.eece4792mealmaster.utils.exceptions.BadRequest;
import com.app.eece4792mealmaster.utils.exceptions.ResourceExistsException;
import com.app.eece4792mealmaster.utils.exceptions.ResourceNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;

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
    if (session.getAttribute(Consts.SessionConsts.USER_ID) == null) {
      throw new BadRequest();
    }
    session.invalidate();
    return new ApiResponse(200, "Logout successful", null);
  }

  public ApiResponse register(User user) {
    if (userRepository.findUserByUsername(user.getUsername()) != null) {
      throw new ResourceExistsException();
    }
    if (userRepository.findUserByEmail(user.getEmail()) != null) {
      throw new ResourceExistsException();
    }
    userRepository.save(user);
    return new ApiResponse(200, "Registration Successful", null);
  }
}
