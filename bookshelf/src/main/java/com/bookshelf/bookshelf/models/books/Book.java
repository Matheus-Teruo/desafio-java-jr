package com.bookshelf.bookshelf.models.books;

import com.bookshelf.bookshelf.models.books.request.RequestCreateBook;
import com.bookshelf.bookshelf.models.books.request.RequestUpdateBook;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "books")
@NoArgsConstructor
@Getter
public class Book {

  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String title;

  private String autor;

  @Column(name = "ano_publicacao")
  private Integer anoPublicacao;

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
}
