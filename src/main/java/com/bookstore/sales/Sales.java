package com.bookstore.sales;

import com.bookstore.common.Stage;
import com.bookstore.customer.Customer;
import jakarta.persistence.*;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.List;

@Entity
@Data
@Table(name = "sales")
public class Sales {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private LocalDate date;
    private String invoice;
    private String type;
    private Long saleBy;

    @Enumerated(EnumType.STRING)
    private Stage stage;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    @JoinColumn(name = "sales_id", referencedColumnName = "id")
    private List<SalesDetails> details;

    @Transient
    private Stage stg;
}
