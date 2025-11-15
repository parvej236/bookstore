package com.bookstore.book;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BookRepository extends JpaRepository<Book, Long> {
    List<Book> findAllByNameContainingIgnoreCaseOrIsbnContainingIgnoreCase(String name, String isbn);
    Book findByIsbn(String isbn);

    @Query(value = "SELECT COUNT(*) FROM book", nativeQuery = true)
    int getTotalCount();
}
