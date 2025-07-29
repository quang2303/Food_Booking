package com.quang.app.JavaWeb_cdquang.exception;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.HandlerMethodValidationException;

@RestControllerAdvice
public class GlobalException {

	// Handle missing parameter
	@ExceptionHandler(MissingServletRequestParameterException.class)
	public ResponseEntity<?> handleMissingParams(MissingServletRequestParameterException ex) {
		String paramName = ex.getParameterName();
		String message = "Missing required parameter: " + paramName;
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("status", "error", "message", message));
	}

	@ExceptionHandler(OperationFailedException.class)
	public ResponseEntity<?> handleOperationFailed(OperationFailedException ex) {
		return ResponseEntity.status(HttpStatus.BAD_REQUEST)
				.body(Map.of("status", "error", "message", ex.getMessage()));
	}

	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<?> handleOperationFailed(IllegalArgumentException ex) {
		return ResponseEntity.status(HttpStatus.BAD_REQUEST)
				.body(Map.of("status", "error", "message", ex.getMessage()));
	}

	// Handle exception valid
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<Map<String, Object>> handleValidationException(MethodArgumentNotValidException ex) {
		Map<String, String> errors = new HashMap<>();

		ex.getBindingResult().getFieldErrors().forEach(error -> {
			String fieldName = error.getField();
			String message = error.getDefaultMessage();
			errors.put(fieldName, message);
		});

		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("status", "error", "message", errors));
	}

	// Handle exception valid
	@ExceptionHandler(HandlerMethodValidationException.class)
	public ResponseEntity<Map<String, Object>> handleMethodValidationException(HandlerMethodValidationException ex) {

		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("status", "error", "message", ex.getMessage()));
	}

	@ExceptionHandler(NotFoundException.class)
	public ResponseEntity<?> handleNotFound(NotFoundException ex) {
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("status", "error", "message", ex.getMessage()));
	}
	
	@ExceptionHandler(AuthorizationDeniedException.class)
	public ResponseEntity<?> handleAuthorizationEX(AuthorizationDeniedException ex) {
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("status", "error", "message", ex.getMessage()));
	}
}
