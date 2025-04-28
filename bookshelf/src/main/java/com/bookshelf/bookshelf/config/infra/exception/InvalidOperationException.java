package com.bookshelf.bookshelf.config.infra.exception;

public class InvalidOperationException extends RuntimeException {

  private final String typeOfError;
  private final String error;

  public InvalidOperationException(String typeOfError, String error) {
    super("Operação inválida: " + typeOfError);
    this.typeOfError = typeOfError;
    this.error = error;
  }

  public String getTypeOfError() {
    return typeOfError;
  }

  public String getError() {
    return error;
  }
}
