package com.bookstore.book;

import com.bookstore.common.Routes;
import com.bookstore.common.SubmitResult;
import com.bookstore.supplier.SupplierService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@AllArgsConstructor
public class BookController {
    private final BookService service;
    private final SupplierService supplierService;

    @PreAuthorize("@permissionEvaluator.hasPermission(authentication, 'BOOK_VIEW')")
    @GetMapping(Routes.BOOK_ENTRY)
    public String bookEntry(@RequestParam(name = "id", required = false) Long id, @RequestParam(name = "msg", required = false) String msg, Model model) {
        Book book = new Book();
        if (id != null) {
            book = service.getById(id);
            if (msg != null && msg.equals("create")) {
                SubmitResult.success(model, "Book created successfully!");
            }
            if (msg != null && msg.equals("update")) {
                SubmitResult.success(model, "Book updated successfully!");
            }
        }
        model.addAttribute("book", book);
        model.addAttribute("suppliers", supplierService.getSupplierList());
        model.addAttribute("entryUrl", Routes.BOOK_ENTRY);
        return "book/book-entry";
    }

    @PreAuthorize("hasAuthority('Admin')")
    @PostMapping(Routes.BOOK_ENTRY)
    public String createBook(Book book, Model model) {
        boolean flag = book.getId() == null;
        try {
            book = service.createBook(book);
            return "redirect:" + Routes.BOOK_ENTRY + "?id=" + book.getId() + "&msg=" + (flag ? "create" : "update");
        } catch (Exception e) {
            if (flag) {
                SubmitResult.error(model, "Book could not be created!");
            } else {
                SubmitResult.error(model, "Book could not be updated!");
            }
        }
        model.addAttribute("book", book);
        model.addAttribute("suppliers", supplierService.getSupplierList());
        model.addAttribute("entryUrl", Routes.BOOK_ENTRY);
        return "book/book-entry";
    }

    @GetMapping(Routes.BOOK_LIST)
    public String bookList(Model model) {
        model.addAttribute("dataUrl", Routes.BOOK_SEARCH);
        model.addAttribute("openUrl", Routes.BOOK_ENTRY);
        return "book/book-list";
    }

    @GetMapping(Routes.BOOK_SEARCH)
    @ResponseBody
    public ResponseEntity<List<Book>> searchBook() {
        return new ResponseEntity<>(service.getBookList(), HttpStatus.OK);
    }
    
    @GetMapping(Routes.BOOK_SEARCH_BY_ISBN)
    @ResponseBody
    public ResponseEntity<Book> bookSearchByIsbn(@RequestParam(name = "isbn") String isbn) {
        return new ResponseEntity<>(service.getBookListByIsbn(isbn), HttpStatus.OK);
    }

    @GetMapping(Routes.BOOK_SEARCH_BY_NAME_OR_ISBN)
    @ResponseBody
    public ResponseEntity<List<Book>> bookSearchByNameOrIsbn(@RequestParam(name = "nameOrIsbn") String nameOrIsbn) {
        return new ResponseEntity<>(service.getBookListByNameOrIsbn(nameOrIsbn), HttpStatus.OK);
    }
}
