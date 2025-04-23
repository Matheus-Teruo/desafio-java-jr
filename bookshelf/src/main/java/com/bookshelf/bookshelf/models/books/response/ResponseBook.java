package com.bookshelf.bookshelf.models.books.response;

import com.bookshelf.bookshelf.models.books.Book;

public record ResponseBook(
    Long id,
    String titulo,
    String autor,
    Integer anoPublicacao
) {

  public ResponseBook(Book book) {
    this(book.getId(),
        book.getTitle(),
        book.getAutor(),
        book.getAnoPublicacao()
    );
  }
}
