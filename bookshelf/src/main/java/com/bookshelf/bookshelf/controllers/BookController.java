package com.bookshelf.bookshelf.controllers;

import com.bookshelf.bookshelf.models.books.Book;
import com.bookshelf.bookshelf.models.books.request.RequestCreateBook;
import com.bookshelf.bookshelf.models.books.request.RequestUpdateBook;
import com.bookshelf.bookshelf.models.books.response.ResponseBook;
import com.bookshelf.bookshelf.repositories.BookRepository;
import com.bookshelf.bookshelf.service.BookService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("books")
public class BookController {

  @Autowired
  private BookService service;

  @Autowired
  private BookRepository repository;

  @PostMapping
  public ResponseEntity<ResponseBook> createBook(@RequestBody @Valid RequestCreateBook request) {
    var book = service.createBook(request);

    URI location = ServletUriComponentsBuilder
        .fromCurrentRequest()
        .path("/{id}")
        .buildAndExpand(book.getId())
        .toUri();

    return ResponseEntity.created(location).body(new ResponseBook(book));
  }

  @GetMapping("/{id}")
  public ResponseEntity<ResponseBook> getBook(@PathVariable @Valid Long id) {
    Book book = repository.findById(id)
        .orElseThrow(EntityNotFoundException::new);

    var response = new ResponseBook(book);
    return ResponseEntity.ok(response);
  }

  @GetMapping
  public ResponseEntity<Page<ResponseBook>> getBooksPage(Pageable pageable) {
    var books = repository.findAll(pageable);

    var response = books.map(ResponseBook::new);
    return ResponseEntity.ok(response);
  }

  @PutMapping
  public ResponseEntity<ResponseBook> updateBook(@RequestBody @Valid RequestUpdateBook request) {
    var response = new ResponseBook(service.updateBook(request));

    return ResponseEntity.ok(response);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteBook(@PathVariable @Valid Long id) {
    service.deleteBook(id);

    return ResponseEntity.noContent().build();
  }
}
