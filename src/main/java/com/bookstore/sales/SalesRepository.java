package com.bookstore.sales;

import com.bookstore.common.Stage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface SalesRepository extends JpaRepository<Sales, Long> {
    int countByDate(LocalDate now);

    @Query(value = "SELECT s.* FROM sales s JOIN sales_details sd ON sd.sales_id = s.id WHERE sd.book_id = :id ORDER BY s.date DESC LIMIT 3", nativeQuery = true)
    List<Sales> getRecentTransactions(@Param(value = "id") Long id);

    List<Sales> findAllByDateAndStage(LocalDate date, Stage stage);

    List<Sales> findAllByDateBetweenAndStage(LocalDate dateAfter, LocalDate dateBefore, Stage stage);
}
