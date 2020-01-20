package com.app.eece4792mealmaster.utils.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.FORBIDDEN, reason = "Forbidden Request")
public class ForbiddenRequestException extends RuntimeException {}
