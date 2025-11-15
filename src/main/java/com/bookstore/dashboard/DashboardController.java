package com.bookstore.dashboard;

import com.bookstore.book.BookService;
import com.bookstore.common.Routes;
import com.bookstore.customer.CustomerService;
import com.bookstore.sales.SalesService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalDate;

@Controller
@AllArgsConstructor
public class DashboardController {
    private final DashboardService service;
    private final SalesService salesService;
    private final CustomerService customerService;
    private final BookService bookService;

    @GetMapping({Routes.ROOT, Routes.HOME, Routes.DASHBOARD})
    public String dashboard(Model model) {
        model.addAttribute("salesToday", salesService.getSalesToday(LocalDate.now()));
        model.addAttribute("revenueForMonth", salesService.getRevenueForMonth());
        model.addAttribute("stock", bookService.getTotalCount());
        model.addAttribute("customerCount", customerService.getTotalCount());
        model.addAttribute("dayList", service.getLast7Days());
        model.addAttribute("weeklyRevenue", salesService.getWeeklyRevenue());
        model.addAttribute("weeklySalesCount", salesService.getWeeklySalesCount());
        return "dashboard/dashboard";
    }
}
