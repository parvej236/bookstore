package com.bookstore.supplier;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "supplier")
public class Supplier {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String type;
    private String phone;
    private String email;
    private String website;
    private String address;
}
