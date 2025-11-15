package com.bookstore.stock;

import com.bookstore.book.Book;
import lombok.Data;

import java.util.List;

@Data
public class Stock {
    private Book book;
    private Integer currentStock;
    private Integer pendingStock;
    private List<StockDetails> details;
}
