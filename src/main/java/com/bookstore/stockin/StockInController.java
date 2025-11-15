package com.bookstore.stockin;

import com.bookstore.book.BookService;
import com.bookstore.common.Routes;
import com.bookstore.common.SubmitResult;
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
public class StockInController {
    private StockInService service;
    private BookService bookService;

    @GetMapping(Routes.STOCK_IN)
    public String stockInEntry(@RequestParam(name = "id", required = false) Long id, @RequestParam(name = "msg", required = false) String msg, Model model) {
        StockIn stockIn = new StockIn();
        if (id == null) {
            stockIn.setInvoice(service.getInvoice());
        } else {
            stockIn = service.findById(id);
            if (msg != null && msg.equals("create")) {
                SubmitResult.success(model, "StockIn created successfully!");
            }
            if (msg != null && msg.equals("update")) {
                SubmitResult.success(model, "StockIn updated successfully!");
            }
        }
        model.addAttribute("stockIn", stockIn);
        model.addAttribute("entryUrl", Routes.STOCK_IN);
        model.addAttribute("isbnSearchUrl", Routes.BOOK_SEARCH_BY_ISBN);
        model.addAttribute("nameOrIsbnSearchUrl", Routes.BOOK_SEARCH_BY_NAME_OR_ISBN);
        return "stockin/stockin-entry";
    }

    @PostMapping(Routes.STOCK_IN)
    public String stockInEntry(@ModelAttribute StockIn stockIn, BindingResult result, Model model) {
        boolean flag = stockIn.getId() == null;
        service.validation(stockIn, result);
        try {
            if (!result.hasErrors()) {
                stockIn = service.save(stockIn);
                return "redirect:" + Routes.STOCK_IN + "?id=" + stockIn.getId() + "&msg=" + (flag ? "create" : "update");
            }
        } catch (Exception e) {
            if (flag) {
                SubmitResult.error(model, "StockIn could not be created!");
            } else {
                SubmitResult.error(model, "StockIn could not be updated!");
            }
        }
        if (stockIn.getDetails() != null) {
            stockIn.getDetails().forEach(detail -> detail.setBook(bookService.getById(detail.getBook().getId())));
        }
        model.addAttribute("stockIn", stockIn);
        model.addAttribute("entryUrl", Routes.STOCK_IN);
        model.addAttribute("isbnSearchUrl", Routes.BOOK_SEARCH_BY_ISBN);
        model.addAttribute("nameOrIsbnSearchUrl", Routes.BOOK_SEARCH_BY_NAME_OR_ISBN);
        return "stockin/stockin-entry";
    }

    @GetMapping(Routes.STOCK_IN_LIST)
    public String stockInList(Model model) {
        model.addAttribute("dataUrl", Routes.STOCK_IN_SEARCH);
        model.addAttribute("openUrl", Routes.STOCK_IN);
        return "stockin/stockin-list";
    }

    @GetMapping(Routes.STOCK_IN_SEARCH)
    @ResponseBody
    public ResponseEntity<List<StockIn>> stockInSearch() {
        return new ResponseEntity<>(service.getStockInList(), HttpStatus.OK);
    }
}
