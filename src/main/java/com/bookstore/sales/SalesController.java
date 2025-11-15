package com.bookstore.sales;

import com.bookstore.book.BookService;
import com.bookstore.common.Routes;
import com.bookstore.common.SubmitResult;
import com.bookstore.customer.CustomerService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@AllArgsConstructor
public class SalesController {
    private final SalesService service;
    private final BookService bookService;
    private final CustomerService customerService;

    @GetMapping(Routes.SALE_ENTRY)
    public String supplierEntry(@RequestParam(name = "id", required = false) Long id, @RequestParam(name = "msg", required = false) String msg, Model model) {
        Sales sales = new Sales();

        if (id == null) {
            sales.setInvoice(service.getInvoice());
        } else {
            sales = service.findById(id);
            sales.setCustomer(customerService.getById(sales.getCustomer().getId()));
            if (msg != null && msg.equals("create")) {
                SubmitResult.success(model, "StockIn created successfully!");
            }
            if (msg != null && msg.equals("update")) {
                SubmitResult.success(model, "StockIn updated successfully!");
            }
        }
        model.addAttribute("sales", sales);
        model.addAttribute("entryUrl", Routes.SALE_ENTRY);
        model.addAttribute("isbnSearchUrl", Routes.BOOK_SEARCH_BY_ISBN);
        model.addAttribute("nameOrIsbnSearchUrl", Routes.BOOK_SEARCH_BY_NAME_OR_ISBN);
        model.addAttribute("customerSearchUrl", Routes.CUSTOMER_SEARCH_BY_NAME_OR_PHONE);
        return "sales/sales-entry";
    }

    @PostMapping(Routes.SALE_ENTRY)
    public String supplierEntry(@ModelAttribute Sales sales, BindingResult result, Model model) {
        boolean flag = sales.getId() == null;
        boolean hasQuantityError = service.validation(sales, result);
        try {
            if (!result.hasErrors()) {
                sales = service.save(sales);
                return "redirect:" + Routes.SALE_ENTRY + "?id=" + sales.getId() + "&msg=" + (flag ? "create" : "update");
            }
        } catch (Exception e) {
             if (flag) {
                SubmitResult.error(model, "Sales could not be created!");
            } else {
                SubmitResult.error(model, "Sales could not be updated!");
            }
        }
        if (sales.getCustomer().getId() != null) {
            sales.setCustomer(customerService.getById(sales.getCustomer().getId()));
        }
        if (sales.getDetails() != null) {
            sales.getDetails().forEach(detail -> detail.setBook(bookService.getById(detail.getBook().getId())));
        }
        model.addAttribute("sales", sales);
        model.addAttribute("entryUrl", Routes.SALE_ENTRY);
        model.addAttribute("isbnSearchUrl", Routes.BOOK_SEARCH_BY_ISBN);
        model.addAttribute("nameOrIsbnSearchUrl", Routes.BOOK_SEARCH_BY_NAME_OR_ISBN);
        model.addAttribute("customerSearchUrl", Routes.CUSTOMER_SEARCH_BY_NAME_OR_PHONE);
        if (hasQuantityError) {
            SubmitResult.error(model, "Some items has less stock!");
        }
        return "sales/sales-entry";
    }

    @GetMapping(Routes.SALE_LIST)
    public String salesList(Model model) {
        model.addAttribute("dataUrl", Routes.SALE_SEARCH);
        model.addAttribute("openUrl", Routes.SALE_ENTRY);
        return "sales/sales-list";
    }

    @GetMapping(Routes.SALE_SEARCH)
    @ResponseBody
    public ResponseEntity<List<Sales>> salesSearch() {
        return new ResponseEntity<>(service.getSalesList(), HttpStatus.OK);
    }
}
