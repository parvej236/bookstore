package com.bookstore.stockin;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface StockInRepository extends JpaRepository<StockIn, Long> {
    int countByDate(LocalDate now);

    @Query(value = "SELECT SUM(sd.quantity) FROM stockin_details AS sd JOIN stockin AS s ON s.id = sd.stockin_id WHERE sd.book_id = :id AND s.stage != 'APPROVE'", nativeQuery = true)
    Integer getPendingStock(@Param(value = "id") Long id);

    @Query(value = "SELECT s.* FROM stockin s JOIN stockin_details sd ON sd.stockin_id = s.id WHERE sd.book_id = :id ORDER BY s.date DESC LIMIT 3", nativeQuery = true)
    List<StockIn> getRecentTransactions(@Param("id") Long id);
}
