package com.bookshelf.bookshelf.service;

import com.bookshelf.bookshelf.BaseTest;
import com.bookshelf.bookshelf.config.infra.exception.InvalidQueryEntityNotFound;
import com.bookshelf.bookshelf.models.books.Book;
import com.bookshelf.bookshelf.models.books.request.RequestUpdateBook;
import com.bookshelf.bookshelf.repositories.BookRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.Optional;

import static com.bookshelf.bookshelf.TestFactory.createRequestCreateBook;
import static com.bookshelf.bookshelf.TestFactory.createRequestUpdateBook;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class BookServiceTest extends BaseTest {

  @InjectMocks
  private BookService service;

  @MockitoBean
  private BookRepository repository;

  @Test
  void shouldCreateBook() {
    // Given
    Book book = new Book(1L, "Dom Casmurro", "Machado", 1899);

    when(repository.save(any(Book.class))).thenReturn(book);

    // When & Then
    Book created = service.createBook(createRequestCreateBook(book));

    // Verify interactions
    assertNotNull(created);
    assertEquals("Dom Casmurro", created.getTitle());
    assertEquals("Machado", created.getAutor());
    assertEquals(1899, created.getAnoPublicacao());
  }

  @Test
  void shouldUpdateBookWhenIdExists() {
    // Given
    Book book = new Book(1L, "Dom Casmurro", "Machado", 1899);

    when(repository.findById(any(Long.class))).thenReturn(Optional.of(book));

    // When & Then
    Book updated = service.updateBook(createRequestUpdateBook(book.getId()));

    // Verify interactions
    assertNotNull(updated);
    assertEquals("Book Update Test", updated.getTitle());
    assertEquals("Autor Update Teste", updated.getAutor());
    assertEquals(2021, updated.getAnoPublicacao());
  }

  @Test
  void shouldThrowExceptionWhenUpdatingNonExistingBook() {
    // Given
    Long nonexistentId = 99L;
    RequestUpdateBook request = new RequestUpdateBook(nonexistentId, "Novo Título", "Novo Autor", 999);

    when(repository.findById(nonexistentId)).thenReturn(Optional.empty());

    // When & Then
    InvalidQueryEntityNotFound exception = assertThrows(
        InvalidQueryEntityNotFound.class,
        () -> service.updateBook(request)
    );

    // Verify interactions
    assertEquals("Valor inválido para a entidade: livro: 99", exception.getMessage());
    verify(repository).findById(nonexistentId);
  }

  @Test
  void shouldDeleteBookWhenIdExists() {
    // Given
    Book mockBook = new Book(1L, "Dom Casmurro", "Machado", 1899);

    when(repository.findById(any(Long.class))).thenReturn(Optional.of(mockBook));
    doNothing().when(repository).delete(mockBook);

    // When & Then
    service.deleteBook(mockBook.getId());

    // Verify interactions
    verify(repository).findById(mockBook.getId());
    verify(repository).delete(mockBook);
  }

  @Test
  void shouldThrowExceptionWhenDeletingNonExistingBook() {
    // Given
    Long nonexistentId = 99L;
    RequestUpdateBook request = new RequestUpdateBook(nonexistentId, "Novo Título", "Novo Autor", 999);

    when(repository.findById(nonexistentId)).thenReturn(Optional.empty());

    // When & Then
    EntityNotFoundException exception = assertThrows(
        EntityNotFoundException.class,
        () -> service.deleteBook(request.id())
    );

    // Verify interactions
    verify(repository).findById(nonexistentId);
    verify(repository, never()).delete(any());
  }
}
