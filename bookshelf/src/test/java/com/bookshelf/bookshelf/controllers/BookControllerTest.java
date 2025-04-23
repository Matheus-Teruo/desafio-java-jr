package com.bookshelf.bookshelf.controllers;

import com.bookshelf.bookshelf.BaseTest;
import com.bookshelf.bookshelf.models.books.Book;
import com.bookshelf.bookshelf.models.books.request.RequestCreateBook;
import com.bookshelf.bookshelf.models.books.request.RequestUpdateBook;
import com.bookshelf.bookshelf.models.books.response.ResponseBook;
import com.bookshelf.bookshelf.repositories.BookRepository;
import com.bookshelf.bookshelf.service.BookService;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.List;
import java.util.Optional;

import static com.bookshelf.bookshelf.TestFactory.*;
import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class BookControllerTest extends BaseTest {

  @MockitoBean
  private BookService service;

  @MockitoBean
  private BookRepository repository;

  @Test
  void testCreateBookSuccess() throws Exception {
    // Given
    Book mockBook = createBookEntity(1L);
    RequestCreateBook requestCreateBook = createRequestCreateBook(mockBook);
    ResponseBook expectedResponse = new ResponseBook(mockBook);

    when(service.createBook(requestCreateBook)).thenReturn(mockBook);

    // When & Then
    mockMvc.perform(post("/books")
            .contentType(MediaType.APPLICATION_JSON)
            .content(toJson(requestCreateBook)))
        .andExpect(status().isCreated())
        .andExpect(header().string("Location",
            containsString("/books/" + mockBook.getId().toString())))
        .andExpect(content().json(toJson(expectedResponse)));


    // Verify interactions
    verify(service, times(1)).createBook(requestCreateBook);
    verifyNoMoreInteractions(service);
    verifyNoMoreInteractions(repository);
  }

  @Test
  void testReadBookSuccess() throws Exception {
    // Given
    Long bookId = 1L;
    Book mockBook = createBookEntity(bookId);
    ResponseBook expectedResponse = new ResponseBook(mockBook);

    when(repository.findById(bookId)).thenReturn(Optional.of(mockBook));

    // When & Then
    mockMvc.perform(get("/books/{id}", bookId)
            .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().json(toJson(expectedResponse)));

    // Verify interactions
    verify(repository, times(1)).findById(bookId);
    verifyNoMoreInteractions(repository);
    verifyNoMoreInteractions(service);
  }

  @Test
  void testReadBookNonExist() throws Exception {
    // Given
    Long nonexistentId = 1L;

    when(repository.findById(nonexistentId)).thenReturn(Optional.empty());

    // When & Then
    mockMvc.perform(get("/books/{id}", nonexistentId)
            .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isNotFound());

    // Verify interactions
    verify(repository, times(1)).findById(nonexistentId);
    verifyNoMoreInteractions(repository);
    verifyNoMoreInteractions(service);
  }

  @Test
  void testReadBooksSuccess() throws Exception {
    // Given
    List<Book> mockBooks = List.of(
        createBookEntity(1L),
        createBookEntity(2L)
    );
    Page<Book> mockPage = new PageImpl<>(mockBooks);
    Page<ResponseBook> expectedResponse = mockPage
        .map(ResponseBook::new);

    when(repository.findAll(any(Pageable.class))).thenReturn(mockPage);

    // When & Then
    mockMvc.perform(get("/books")
            .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.content.length()").value(2))
        .andExpect(content().json(toJson(expectedResponse)));

    // Verify interactions
    verify(repository, times(1)).findAll(any(Pageable.class));
    verifyNoMoreInteractions(repository);
    verifyNoMoreInteractions(service);
  }

  @Test
  void testUpdateBooksSuccess() throws Exception {
    // Given
    Book mockBook = createBookEntity(1L);
    RequestUpdateBook updateRequest = createRequestUpdateBook(mockBook.getId());

    mockBook.update(updateRequest);
    ResponseBook expectedResponse = new ResponseBook(mockBook);

    when(service.updateBook(updateRequest)).thenReturn(mockBook);

    // When & Then
    mockMvc.perform(put("/books")
            .contentType(MediaType.APPLICATION_JSON)
            .content(toJson(updateRequest)))
        .andExpect(status().isOk())
        .andExpect(content().json(toJson(expectedResponse)));

    // Verify interactions
    verify(service, times(1)).updateBook(updateRequest);
    verifyNoMoreInteractions(service);
    verifyNoMoreInteractions(repository);
  }

  @Test
  void testDeleteProductSuccess() throws Exception {
    // Given
    Long bookId = 1L;

    doNothing().when(service).deleteBook(bookId);

    // When & Then
    mockMvc.perform(delete("/books/{id}", bookId)
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isNoContent());

    // Verify interactions
    verify(service, times(1)).deleteBook(bookId);
    verifyNoMoreInteractions(service);
    verifyNoMoreInteractions(repository);
  }
}