package com.app.eece4792mealmaster.controllers;

import com.app.eece4792mealmaster.dto.MealDto;
import com.app.eece4792mealmaster.services.MealService;
import com.app.eece4792mealmaster.utils.ApiResponse;
import com.app.eece4792mealmaster.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpSession;

import static com.app.eece4792mealmaster.constants.Routes.CONSUME;
import static com.app.eece4792mealmaster.constants.Routes.MEAL_API;

@RestController
@CrossOrigin(origins = {"*", "http://localhost:3000"}, allowCredentials = "true")
public class MealController {
    @Autowired
    MealService mealService;

    @PostMapping(MEAL_API + CONSUME)
    public ApiResponse consume(HttpSession session, @RequestBody MealDto mealData) {
        Long userId = Utils.getLoggedInUser(session);
        if (userId == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
        if (mealData == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        mealService.consume(userId, mealData);

        return new ApiResponse();
    }
}
