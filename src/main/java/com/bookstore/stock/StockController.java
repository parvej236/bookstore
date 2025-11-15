package com.bookstore.stock;

import com.bookstore.book.Book;
import com.bookstore.book.BookService;
import com.bookstore.common.Routes;
import com.bookstore.sales.Sales;
import com.bookstore.sales.SalesDetails;
import com.bookstore.sales.SalesService;
import com.bookstore.stockin.StockIn;
import com.bookstore.stockin.StockInDetails;
import com.bookstore.stockin.StockInService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Controller
@AllArgsConstructor
public class StockController {
    private final BookService bookService;
    private final StockInService stockInService;
    private final SalesService salesService;

    @GetMapping(Routes.CURRENT_STOCK)
    public String currentStock(Model model) {
        model.addAttribute("isbnSearchUrl", Routes.BOOK_SEARCH_BY_ISBN);
        model.addAttribute("nameOrIsbnSearchUrl", Routes.BOOK_SEARCH_BY_NAME_OR_ISBN);
        model.addAttribute("dataUrl", Routes.CURRENT_STOCK_DETAILS);
        return "stock/current-stock";
    }

    @GetMapping(Routes.CURRENT_STOCK_DETAILS)
    @ResponseBody
    public ResponseEntity<Stock> currentStockDetails(@RequestParam(name = "id") Long id) {
        Stock stock = new Stock();
        Book book = bookService.getById(id);
        stock.setBook(book);
        stock.setCurrentStock(book.getStock());
        Integer pendingStock = stockInService.getPendingStock(id);
        stock.setPendingStock(pendingStock == null ? 0 : pendingStock);
        List<StockIn> stockInList = stockInService.getRecentTransactions(id);
        List<Sales> salesList = salesService.getRecentTransactions(id);
        List<StockDetails> stockDetails = new ArrayList<>();

        if (!stockInList.isEmpty() && !salesList.isEmpty()) {
            stockInList.forEach(s -> {
                StockDetails sd = new StockDetails();
                sd.setDate(s.getDate());
                sd.setInvoice(s.getInvoice());
                sd.setType("Stock In");
                sd.setPurpose(sd.getType());
                StockInDetails details = s.getDetails().stream().filter(d -> d.getBook().getId().equals(id)).findFirst().orElse(null);
                sd.setPreStock(details.getPreStock());
                sd.setQuantity(details.getQuantity());
                stockDetails.add(sd);
            });

            salesList.forEach(s -> {
                StockDetails sd = new StockDetails();
                sd.setDate(s.getDate());
                sd.setInvoice(s.getInvoice());
                sd.setType("Sales");
                sd.setPurpose(sd.getType());
                SalesDetails details = s.getDetails().stream().filter(d -> d.getBook().getId().equals(id)).findFirst().orElse(null);
                sd.setPreStock(details.getPreStock());
                sd.setQuantity(details.getQuantity());
                stockDetails.add(sd);
            });

            stockDetails.sort(Comparator.comparing(StockDetails::getDate));

            stock.setDetails(stockDetails);
        }
        return new ResponseEntity<>(stock, HttpStatus.OK);
    }
}
