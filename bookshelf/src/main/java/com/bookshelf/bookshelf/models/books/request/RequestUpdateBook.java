package com.bookshelf.bookshelf.models.books.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record RequestUpdateBook(
    @NotNull(message = "O livro precisa ser identificado pelo id.")
    Long id,

    String title,

    @Pattern(regexp = "^[\\p{L}\\p{N} ]*$", message = "O nome do autor do livro só pode conter letras números e espaço.")
    String autor,

    Integer anoPublicacao
) {
}
