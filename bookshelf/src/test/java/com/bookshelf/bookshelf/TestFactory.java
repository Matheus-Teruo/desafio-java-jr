package com.bookshelf.bookshelf;

import com.bookshelf.bookshelf.models.books.Book;
import com.bookshelf.bookshelf.models.books.request.RequestCreateBook;
import com.bookshelf.bookshelf.models.books.request.RequestUpdateBook;

public class TestFactory {

  public static Book createBookEntity(Long id) {
    return new Book(
        id,
        "Book Teste",
        "Autor Teste",
        2020
    );
  }

  public static RequestCreateBook createRequestCreateBook(Book book) {
    return new RequestCreateBook(
        book.getTitle(),
        book.getAutor(),
        book.getAnoPublicacao()
    );
  }

  public static RequestUpdateBook createRequestUpdateBook(Long id) {
    return new RequestUpdateBook(
        id,
        "Book Update Test",
        "Autor Update Teste",
        2021
    );
  }
}
