package com.bookstore.sales;

import com.bookstore.book.Book;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "sales_details")
public class SalesDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "book_id")
    private Book book;

    private Integer preStock;
    private Integer quantity;
}
