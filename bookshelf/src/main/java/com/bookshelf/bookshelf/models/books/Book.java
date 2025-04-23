package com.bookshelf.bookshelf.models.books;

import com.bookshelf.bookshelf.models.books.request.RequestCreateBook;
import com.bookshelf.bookshelf.models.books.request.RequestUpdateBook;
import jakarta.persistence.*;

@Entity
@Table(name = "books")
public class Book {

  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String title;

  private String autor;

  @Column(name = "ano_publicacao")
  private Integer anoPublicacao;

  public Book() {}

  public Book(Long id, String title, String autor, Integer anoPublicacao) {
    this.id = id;
    this.title = title;
    this.autor = autor;
    this.anoPublicacao = anoPublicacao;
  }

  public Book(RequestCreateBook createBook) {
    this.title = createBook.title();
    this.autor = createBook.autor();
    this.anoPublicacao = createBook.anoPublicacao();
  }

  public void update(RequestUpdateBook updateBook) {
    if (updateBook.title() != null) {
      this.title = updateBook.title();
    }
    if (updateBook.autor() != null) {
      this.autor = updateBook.autor();
    }
    if (updateBook.anoPublicacao() != null) {
      this.anoPublicacao = updateBook.anoPublicacao();
    }
  }

  public Long getId() {
    return id;
  }

  public String getTitle() {
    return title;
  }

  public String getAutor() {
    return autor;
  }

  public Integer getAnoPublicacao() {
    return anoPublicacao;
  }
}
