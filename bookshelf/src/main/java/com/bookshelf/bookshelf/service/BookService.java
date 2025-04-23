package com.bookshelf.bookshelf.service;

import com.bookshelf.bookshelf.config.infra.exception.InvalidQueryEntityNotFound;
import com.bookshelf.bookshelf.models.books.Book;
import com.bookshelf.bookshelf.models.books.request.RequestCreateBook;
import com.bookshelf.bookshelf.models.books.request.RequestUpdateBook;
import com.bookshelf.bookshelf.repositories.BookRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BookService {

  @Autowired
  private BookRepository repository;

  @Transactional
  public Book createBook(RequestCreateBook createBook) {
    Book newBook = new Book(createBook);

    repository.save(newBook);
    return newBook;
  }

  @Transactional
  public Book updateBook(RequestUpdateBook updateBook) {
    Book book = repository.findById(updateBook.id())
        .orElseThrow(() -> new InvalidQueryEntityNotFound("livro", updateBook.id().toString()));

    book.update(updateBook);

    return book;
  }

  @Transactional
  public void deleteBook(Long id) {
    Book book = repository.findById(id)
        .orElseThrow(EntityNotFoundException::new);

    repository.delete(book);
  }
}
