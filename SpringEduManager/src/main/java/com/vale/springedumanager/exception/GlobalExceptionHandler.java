package com.vale.springedumanager.exception;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
	
	@ExceptionHandler(RecursoNoEncontradoException.class)
	public ResponseEntity<Map<String, String>> handleNotFound(RecursoNoEncontradoException ex){
		return ResponseEntity
				.status(HttpStatus.NOT_FOUND)
				.body(Map.of("Error", ex.getMessage()));
	}
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<Map<String, String>> handleValidation(MethodArgumentNotValidException ex) {
		Map<String, String> errores = new HashMap<>();
		
		ex.getBindingResult()
		.getFieldErrors()
		.forEach(error -> errores.put(error.getField(), error.getDefaultMessage()));
		
		return ResponseEntity.badRequest().body(errores);
	}

}

