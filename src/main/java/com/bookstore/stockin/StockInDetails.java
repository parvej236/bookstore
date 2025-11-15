package com.bookstore.stockin;

import com.bookstore.book.Book;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "stockin_details")
public class StockInDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "book_id")
    private Book book;

    private Integer preStock;
    private Integer quantity;
}
