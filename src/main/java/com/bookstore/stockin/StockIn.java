package com.bookstore.stockin;

import com.bookstore.common.Stage;
import jakarta.persistence.*;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Entity
@Data
@Table(name = "stockin")
public class StockIn {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private LocalDate date;
    private String invoice;
    private String type;

    @Enumerated(EnumType.STRING)
    private Stage stage;

    private Date createdAt;
    private String createdBy;
    private Date submittedAt;
    private String submittedBy;
    private Date approvedAt;
    private String approvedBy;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    @JoinColumn(name = "stockin_id", referencedColumnName = "id")
    private List<StockInDetails> details;

    @Transient
    private Stage stg;
}
