package com.bookstore.stockin;

import com.bookstore.book.Book;
import com.bookstore.book.BookService;
import com.bookstore.common.Stage;
import com.bookstore.config.GlobalAdviceConfig;
import com.bookstore.user.User;
import com.bookstore.user.UserService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Service
@AllArgsConstructor
public class StockInService {
    private StockInRepository repository;
    private BookService bookService;
    private UserService userService;

    public List<StockIn> getStockInList() {
        return repository.findAll(Sort.by(Sort.Direction.DESC, "id"));
    }

    public String getInvoice() {
        return repository.countByDate(LocalDate.now()) + 100 + LocalDate.now().format(DateTimeFormatter.ofPattern("ddMMyyyy"));
    }

    public StockIn save(StockIn stockIn) {
        return repository.save(stockIn);
    }

    public StockIn findById(Long id) {
        return repository.findById(id).orElse(null);
    }

    public void validation(StockIn stockIn, BindingResult result) {
        if (stockIn.getInvoice().trim().isEmpty()) {
            result.rejectValue("invoice", null, "Required!");
        }
        if (stockIn.getDate() == null) {
            result.rejectValue("date", null, "Required!");
        }
        if (stockIn.getType().trim().isEmpty()) {
            result.rejectValue("type", null, "Required!");
        }
        if (stockIn.getDetails() == null) {
            result.rejectValue("details", null, "");
        }

        if (result.hasErrors()) {
            return;
        }

        if (stockIn.getId() != null) {
            StockIn s = findById(stockIn.getId());
            stockIn.setCreatedAt(s.getCreatedAt());
            stockIn.setCreatedBy(s.getCreatedBy());
            stockIn.setSubmittedAt(s.getSubmittedAt());
            stockIn.setSubmittedBy(s.getSubmittedBy());
            stockIn.setApprovedAt(s.getApprovedAt());
            stockIn.setApprovedBy(s.getApprovedBy());
        }

        User user = userService.findByUsername(Objects.requireNonNull(GlobalAdviceConfig.loggedUser()).getUsername());
        if (stockIn.getStg().equals(Stage.SAVE)) {
            stockIn.setCreatedAt(new Date());
            stockIn.setCreatedBy(user.getName());
            stockIn.setStage(Stage.SAVE);
        }
        if (stockIn.getStg().equals(Stage.SUBMIT)) {
            stockIn.setSubmittedAt(new Date());
            stockIn.setSubmittedBy(user.getName());
            stockIn.setStage(Stage.SUBMIT);
        }
        if (stockIn.getStg().equals(Stage.APPROVE)) {
            stockIn.setApprovedAt(new Date());
            stockIn.setApprovedBy(user.getName());
            stockIn.setStage(Stage.APPROVE);

            stockIn.getDetails().forEach(detail -> {
                Book book = bookService.getById(detail.getBook().getId());
                book.setStock(detail.getQuantity() + book.getStock());
                bookService.createBook(book);
            });
        }
    }

    public Integer getPendingStock(Long id) {
        return repository.getPendingStock(id);
    }

    public List<StockIn> getRecentTransactions(Long id) {
        return repository.getRecentTransactions(id);
    }
}
