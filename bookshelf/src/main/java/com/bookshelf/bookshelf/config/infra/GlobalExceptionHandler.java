package com.bookshelf.bookshelf.config.infra;

import com.bookshelf.bookshelf.config.infra.exception.InvalidQueryEntityNotFound;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
}
