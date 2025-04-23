package com.bookshelf.bookshelf.repositories;

import com.bookshelf.bookshelf.models.books.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, Long> {
}
