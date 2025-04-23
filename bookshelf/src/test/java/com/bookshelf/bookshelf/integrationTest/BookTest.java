package com.bookshelf.bookshelf.integrationTest;

import com.bookshelf.bookshelf.BaseTest;
import com.bookshelf.bookshelf.models.books.Book;
import com.bookshelf.bookshelf.models.books.request.RequestCreateBook;
import com.bookshelf.bookshelf.models.books.request.RequestUpdateBook;
import com.bookshelf.bookshelf.models.books.response.ResponseBook;
import com.bookshelf.bookshelf.repositories.BookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static com.bookshelf.bookshelf.TestFactory.createBookEntity;
import static com.bookshelf.bookshelf.TestFactory.createRequestCreateBook;
import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class BookTest extends BaseTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private BookRepository repository;

  @BeforeEach
  void setup() {
    repository.deleteAll();
  }

  @Test
  void testIntegrationCreateBookSuccess() throws Exception {
    // Given
    Book mockBook = createBookEntity(null);
    RequestCreateBook requestCreateBook = createRequestCreateBook(mockBook);
    ResponseBook expectedResponse = new ResponseBook(mockBook);

    // When & Then
    mockMvc.perform(post("/books")
            .contentType(MediaType.APPLICATION_JSON)
            .content(toJson(requestCreateBook)))
        .andExpect(status().isCreated())
        .andExpect(header().string("Location",
            containsString("/books/")));


    // Verify interactions
    List<Book> books = repository.findAll();
    assertEquals(1, books.size());
    assertEquals(expectedResponse.titulo(), books.getFirst().getTitle());
    assertEquals(expectedResponse.autor(), books.getFirst().getAutor());
    assertEquals(expectedResponse.anoPublicacao(), books.getFirst().getAnoPublicacao());
  }

  @Test
  void testIntegrationReadBookByIdSuccess() throws Exception {
    // Given
    Book savedBook = repository.save(createBookEntity(null));
    ResponseBook expectedResponse = new ResponseBook(savedBook);

    // When & Then
    mockMvc.perform(get("/books/{id}", savedBook.getId())
            .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().json(toJson(expectedResponse)));
  }

  @Test
  void testIntegrationReadBooksPaginated() throws Exception {
    // Given
    repository.saveAll(List.of(
        createBookEntity(null),
        createBookEntity(null),
        createBookEntity(null)
    ));

    // When & Then
    mockMvc.perform(get("/books")
            .param("page", "0")
            .param("size", "2")
            .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.content").isArray())
        .andExpect(jsonPath("$.content.length()").value(2))
        .andExpect(jsonPath("$.totalElements").value(3));
  }

  @Test
  void testIntegrationUpdateBookSuccess() throws Exception {
    // Given
    Book savedBook = repository.save(createBookEntity(null));

    RequestUpdateBook updateRequest = new RequestUpdateBook(
        savedBook.getId(),
        "Novo Título",
        savedBook.getAutor(),
        savedBook.getAnoPublicacao()
    );

    // When & Then
    mockMvc.perform(put("/books")
            .contentType(MediaType.APPLICATION_JSON)
            .content(toJson(updateRequest)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.titulo").value("Novo Título"));

    // Verifica se persistiu
    Book updatedBook = repository.findById(savedBook.getId()).orElseThrow();
    assertEquals("Novo Título", updatedBook.getTitle());
  }

  @Test
  void testIntegrationDeleteBookSuccess() throws Exception {
    // Given
    Book savedBook = repository.save(createBookEntity(null));
    Long bookId = savedBook.getId();

    // When & Then
    mockMvc.perform(delete("/books/{id}", bookId))
        .andExpect(status().isNoContent());

    // Verifica se foi removido
    assertFalse(repository.findById(bookId).isPresent());
  }
}
