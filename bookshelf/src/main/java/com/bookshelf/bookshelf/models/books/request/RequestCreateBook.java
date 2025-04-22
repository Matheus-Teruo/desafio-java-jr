package com.bookshelf.bookshelf.models.books.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record RequestCreateBook(
    @NotBlank(message = "O livro precisa ter um titulo.")
    String title,

    @NotBlank(message = "O nome do autor do livro é obrigatório.")
    @Pattern(regexp = "^[\\p{L}\\p{N} ]*$", message = "O nome do autor do livro só pode conter letras números e espaço.")
    String autor,

    @NotBlank(message = "O ano de publicação do livro é obrigatório.")
    Integer anoPublicacao
) {
}
