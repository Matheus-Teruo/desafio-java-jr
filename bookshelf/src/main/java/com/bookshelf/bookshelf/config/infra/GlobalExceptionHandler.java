package com.bookshelf.bookshelf.config.infra;

import com.bookshelf.bookshelf.config.infra.exception.InvalidQueryEntityNotFound;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(EntityNotFoundException.class)
  public ResponseEntity<Void> handleError404(EntityNotFoundException ex) {
    return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
  }

  @ExceptionHandler(InvalidQueryEntityNotFound.class)
  public ResponseEntity<Map<String, String>> handleDatabaseQueryExceptions(InvalidQueryEntityNotFound ex) {
    Map<String, String> error = new HashMap<>();
    error.put("tipoDeErro", "Erro ao buscar entidade no banco de dados");
    error.put("entidade", ex.getEntityName());
    error.put("valorInvalido", ex.getInvalidValue());
    error.put("message", ex.getMessage());

    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<Map<String, Object>> handleValidationExceptions(MethodArgumentNotValidException ex) {
    Map<String, String> errors = new HashMap<>();

    for (FieldError error : ex.getBindingResult().getFieldErrors()) {
      errors.put(error.getField(), error.getDefaultMessage());
    }

    Map<String, Object> response = new HashMap<>();
    response.put("status", HttpStatus.BAD_REQUEST.value());
    response.put("errorType",
        MessageResolver.getInstance().getMessage(
            "exception.global.methodNotValid.errorType")
    );
    response.put("invalidFields", errors);
    response.put("error", MessageResolver.getInstance().getMessage("exception.global.methodNotValid.error"));
    response.put("message", MessageResolver.getInstance().getMessage("exception.global.methodNotValid.message"));


    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
  }
}
