package com.bookstore.stock;

import lombok.Data;

import java.time.LocalDate;

@Data
public class StockDetails {
    private LocalDate date;
    private String invoice;
    private String type;
    private String purpose;
    private Integer preStock;
    private Integer quantity;
}
