package com.app.eece4792mealmaster.utils;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

// For use in long transactional processes
@ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
public class InternalServerError extends RuntimeException {}
