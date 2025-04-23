package com.bookshelf.bookshelf.config.infra.exception;

public class InvalidQueryEntityNotFound extends RuntimeException {

  private final String entityName;
  private final String invalidValue;

  public InvalidQueryEntityNotFound(String entityName, String invalidValue) {
    super("Valor inválido para a entidade: " + entityName + ": " + invalidValue);
    this.entityName = entityName;
    this.invalidValue = invalidValue;
  }

  public String getInvalidValue() {
    return invalidValue;
  }

  public String getEntityName() {
    return entityName;
  }
}
