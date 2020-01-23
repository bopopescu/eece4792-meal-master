package com.app.eece4792mealmaster.controllers;

import com.app.eece4792mealmaster.models.User;
import com.app.eece4792mealmaster.services.UserService;
import com.app.eece4792mealmaster.utils.ApiResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

import static com.app.eece4792mealmaster.controllers.Routes.*;

// TODO: Add EC2 hostname to allowed origins
@RestController
@CrossOrigin(origins = {"*", "http://localhost:3000"}, allowCredentials = "true")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping(USER_API + SEARCH)
    public ApiResponse searchUsers(@RequestParam(SEARCHTERMS) String searchTerms) {
        return userService.searchUsers(searchTerms);
    }


    @GetMapping(PROFILE)
    public ApiResponse profile(HttpSession session) {
        return userService.profile(session);
    }

    // Set username on payload to whatever form field is, accept username and email
    @PostMapping(LOGIN)
    public ApiResponse login(HttpSession session, @RequestBody User userCredentials) {
        String usernameEmail = userCredentials.getUsername() == null ? userCredentials.getEmail() : userCredentials.getUsername();
        return userService.login(session, usernameEmail, userCredentials.getPassword());
    }

    @PostMapping(LOGOUT)
    public ApiResponse logout(HttpSession session) {
        return userService.logout(session);
    }

    @PostMapping(REGISTER)
    public ApiResponse register(@RequestBody User user) {
        return userService.register(user);
    }

    @PutMapping(PROFILE)
    public ApiResponse update(HttpSession session, @RequestBody User userData) {
        return userService.updateProfile(session, userData);
    }

    @DeleteMapping(PROFILE)
    public ApiResponse delete(HttpSession session) {
        return userService.deleteProfile(session);
    }
}
