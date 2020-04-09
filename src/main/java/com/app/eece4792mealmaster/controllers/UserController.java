package com.app.eece4792mealmaster.controllers;

import com.app.eece4792mealmaster.constants.Consts;
import com.app.eece4792mealmaster.models.FoodStock;
import com.app.eece4792mealmaster.models.User;
import com.app.eece4792mealmaster.services.RecipeService;
import com.app.eece4792mealmaster.services.StockService;
import com.app.eece4792mealmaster.services.UserService;
import com.app.eece4792mealmaster.utils.ApiResponse;

import com.app.eece4792mealmaster.utils.Utils;
import com.app.eece4792mealmaster.utils.Views;
import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpSession;

import java.util.Collection;
import java.util.Date;

import static com.app.eece4792mealmaster.constants.Routes.*;

// TODO: Add EC2 hostname to allowed origins
@RestController
@CrossOrigin(origins = {"*", "http://localhost:3000"}, allowCredentials = "true")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private StockService stockService;

    @Autowired
    private RecipeService recipeService;

    @GetMapping(USER_API + VAR_USER_ID)
    @JsonView(Views.Detailed.class)
    public ApiResponse getUser(@PathVariable(USER_ID) Long userId) {
        User user = userService.findById(userId);
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        } else {
            return new ApiResponse(user);
        }
    }

    @GetMapping(USER_API + SEARCH)
    @JsonView(Views.Summary.class)
    public ApiResponse searchUsers(@RequestParam(SEARCHTERMS) String searchTerms) {
        return new ApiResponse(userService.searchUsers(searchTerms));
    }


    @GetMapping(PROFILE)
    @JsonView(Views.Internal.class)
    public ApiResponse profile(HttpSession session) {
        Long userId = Utils.getLoggedInUser(session);
        if (userId == null) { throw new ResponseStatusException(HttpStatus.UNAUTHORIZED); }
        User userProfile = userService.findById(userId);
        return new ApiResponse(userProfile);
    }

    // Set username on payload to whatever form field is, accept username and email
    @PostMapping(LOGIN)
    @JsonView(Views.Internal.class)
    public ApiResponse login(HttpSession session, @RequestBody User userCredentials) {
        String usernameEmail = userCredentials.getUsername() == null ? userCredentials.getEmail() : userCredentials.getUsername();
        User authenticatedUser = userService.authenticatedUser(usernameEmail, userCredentials.getPassword());
        if (authenticatedUser == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
        session.setAttribute(Consts.SessionConsts.USER_ID, authenticatedUser.getId());
        return new ApiResponse("Login Successful", authenticatedUser);
    }

    @PostMapping(LOGOUT)
    public ApiResponse logout(HttpSession session) {
        Long userId = Utils.getLoggedInUser(session);
        if (userId == null) { throw new ResponseStatusException(HttpStatus.UNAUTHORIZED); }
        session.invalidate();
        return new ApiResponse("Logged Out");
    }

    @PostMapping(REGISTER)
    @JsonView(Views.Internal.class)
    public ApiResponse register(@RequestBody User user) {
        if (user == null || user.getUsername() == null || user.getEmail() == null || user.getPassword() == null) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Malformed Body");
        }
        if (userService.createProfile(user)) {
            return new ApiResponse("Registration successful");
        } else {
            throw new ResponseStatusException(HttpStatus.CONFLICT);
        }
    }

    @PutMapping(PROFILE)
    @JsonView(Views.Internal.class)
    public ApiResponse update(HttpSession session, @RequestBody User userData) {
        Long userId = Utils.getLoggedInUser(session);
        if (userId == null) {
            throw new ResponseStatusException(HttpStatus.CONFLICT);
        }
        User updatedProfile = userService.updateProfile(userId, userData);
        if (updatedProfile == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        } else {
            return new ApiResponse(updatedProfile);
        }
    }

    @DeleteMapping(PROFILE)
    public ApiResponse delete(HttpSession session) {
        Long userId = Utils.getLoggedInUser(session);
        if (userId == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
        if (userService.deleteProfile(userId)) {
            session.invalidate();
            return new ApiResponse(String.format("User %d successfully deleted", userId));
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping(INSIGHTS)
    public ApiResponse getUserInsights(HttpSession session)
    {
        Long userId = Utils.getLoggedInUser(session);
        if (userId == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }

        UserInsight userInsight = new UserInsight(userId);
        return new ApiResponse(userInsight);
    }

    public class UserInsight {

        public UserInsight(){}

        public UserInsight(Long userId)
        {
            numExpiredStockItems = stockService.getNumOfExpiredStockItems(userId);
            numStocks = stockService.getStockByUser(userId).size();
            numRecipes = recipeService.getUserLikedRecipes(userId).size();
        }

        private int numExpiredStockItems;

        private int numStocks;

        private int numRecipes;

        public int getNumExpiredStockItems() {
            return numExpiredStockItems;
        }

        public void setNumExpiredStockItems(int numExpiredStockItems) {
            this.numExpiredStockItems = numExpiredStockItems;
        }

        public int getNumStocks() {
            return numStocks;
        }

        public void setNumStocks(int numStocks) {
            this.numStocks = numStocks;
        }

        public int getNumRecipes() {
            return numRecipes;
        }

        public void setNumRecipes(int numRecipes) {
            this.numRecipes = numRecipes;
        }
    }
}


