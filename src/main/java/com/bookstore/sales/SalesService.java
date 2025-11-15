package com.bookstore.sales;

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
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@AllArgsConstructor
public class SalesService {
    private final SalesRepository repository;
    private final BookService bookService;
    private final UserService userService;

    public String getInvoice() {
        return repository.countByDate(LocalDate.now()) + 100 + LocalDate.now().format(DateTimeFormatter.ofPattern("ddMMyyyy"));
    }

    public Sales save(Sales sales) {
        return repository.save(sales);
    }

    public boolean validation(Sales sales, BindingResult result) {
        boolean flag = false;

        if (sales.getInvoice().trim().isEmpty()) {
            result.rejectValue("invoice", null, "Required!");
        }
        if(sales.getDate() == null) {
            result.rejectValue("date", null, "Required!");
        }
        if(sales.getType().trim().isEmpty()) {
            result.rejectValue("type", null, "Required!");
        }
        if (sales.getCustomer().getId() == null) {
            result.rejectValue("customer", null, "Required!");
        }
        if (sales.getDetails() == null) {
            result.rejectValue("details", null, "");
        }

        for (int i = 0; i < sales.getDetails().size(); i++) {
            if (sales.getDetails().get(i).getPreStock() < sales.getDetails().get(i).getQuantity()) {
                result.rejectValue("details[" + i + "].quantity", null, "");
                flag = true;
            }
        }

        if (result.hasErrors()) {
            return flag;
        }

        User user = userService.findByUsername(Objects.requireNonNull(GlobalAdviceConfig.loggedUser()).getUsername());
        if (sales.getStg().equals(Stage.SAVE)) {
            sales.setSaleBy(user.getId());
            sales.setStage(Stage.SAVE);
        }
        if (sales.getStg().equals(Stage.SUBMIT)) {
            sales.setSaleBy(user.getId());
            sales.setStage(Stage.SUBMIT);

            sales.getDetails().forEach(detail -> {
                Book book = bookService.getById(detail.getBook().getId());
                book.setStock(book.getStock() - detail.getQuantity());
                bookService.createBook(book);
            });
        }
        return flag;
    }

    public Sales findById(Long id) {
        return repository.findById(id).orElse(null);
    }

    public List<Sales> getSalesList() {
        return repository.findAll(Sort.by(Sort.Direction.DESC, "id"));
    }

    public List<Sales> getRecentTransactions(Long id) {
        return repository.getRecentTransactions(id);
    }

    public double getSalesToday(LocalDate date) {
        double salesToday = 0.0;
        List<Sales> salesList = repository.findAllByDateAndStage(date, Stage.SUBMIT);

        for (Sales sales : salesList) {
            for (SalesDetails salesDetails : sales.getDetails()) {
                Book book = bookService.getById(salesDetails.getBook().getId());
                salesToday += salesDetails.getQuantity() * (book.getPrice() - book.getCost());
            }
        }
        return salesToday;
    }

    public double getRevenueForMonth() {
        double revenueForMonth = 0.0;
        List<Sales> salesList = repository.findAllByDateBetweenAndStage(LocalDate.now().minusMonths(1), LocalDate.now(), Stage.SUBMIT);

        for (Sales sales : salesList) {
            for (SalesDetails salesDetails : sales.getDetails()) {
                Book book = bookService.getById(salesDetails.getBook().getId());
                revenueForMonth += salesDetails.getQuantity() * (book.getPrice() - book.getCost());
            }
        }
        return revenueForMonth;
    }

    public List<Double> getWeeklyRevenue() {
        List<Double> weeklyRevenue = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            weeklyRevenue.add(getSalesToday(LocalDate.now().minusDays(i)));
        }
        return weeklyRevenue;
    }

    public List<Integer> getWeeklySalesCount() {
        List<Integer> weeklySalesCount = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            Integer count = 0;
            List<Sales> salesList = repository.findAllByDateAndStage(LocalDate.now().minusDays(i), Stage.SUBMIT);
            for(Sales s : salesList) {
                for (SalesDetails sd : s.getDetails()) {
                    count += sd.getQuantity();
                }
            }
            weeklySalesCount.add(count);
        }
        return weeklySalesCount;
    }
}
