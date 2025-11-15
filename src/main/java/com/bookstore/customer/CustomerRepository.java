package com.bookstore.customer;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
    List<Customer> findAllByNameContainingIgnoreCaseOrPhoneContainingIgnoreCase(String name, String phone);

    @Query(value = "SELECT COUNT(*) FROM customer", nativeQuery = true)
    int getTotalCount();
}
