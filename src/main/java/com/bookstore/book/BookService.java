package com.bookstore.book;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class BookService {
    private final BookRepository repository;

    public Book createBook(Book book) {
        if (book.getId() == null) {
            book.setStock(0);
        } else {
            Book b = getById(book.getId());
            book.setStock(b.getStock());
        }
        return repository.save(book);
    }

    public List<Book> getBookList() {
        return repository.findAll();
    }

    public Book getById(Long id) {
        return repository.findById(id).orElse(null);
    }

    public Book getBookListByIsbn(String isbn) {
        return repository.findByIsbn(isbn);
    }

    public List<Book> getBookListByNameOrIsbn(String nameOrIsbn) {
        return repository.findAllByNameContainingIgnoreCaseOrIsbnContainingIgnoreCase(nameOrIsbn, nameOrIsbn);
    }

    public int getTotalCount() {
        return repository.getTotalCount();
    }
}
