package com.bookshelf.bookshelf.service.validation;

import com.bookshelf.bookshelf.config.infra.exception.InvalidOperationException;
import org.springframework.stereotype.Component;

import java.time.Year;

@Component
public class BookValidation {
  public void checkValidPublicationYear(Integer anoPublicacao) {
    int anoAtual = Year.now().getValue();

    if (anoPublicacao > anoAtual) {
      throw new InvalidOperationException("Erro ao inserir dado", "Ano de publicação maior que o atual");
    }
  }
}
