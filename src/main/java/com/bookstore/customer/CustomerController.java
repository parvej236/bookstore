package com.bookstore.customer;

import com.bookstore.common.Routes;
import com.bookstore.common.SubmitResult;
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
public class CustomerController {
    private final CustomerService service;

    @GetMapping(Routes.CUSTOMER_ENTRY)
    public String customerEntry(@RequestParam(name = "id", required = false) Long id,
                                @RequestParam(name = "msg", required = false) String msg,
                                Model model) {
        if (id == null) {
            model.addAttribute("customer", new Customer());
        } else {
            model.addAttribute("customer", service.getById(id));
            if (msg != null && msg.equals("create")) {
                SubmitResult.success(model, "Customer created successfully!");
            }
            if (msg != null && msg.equals("update")) {
                SubmitResult.success(model, "Customer updated successfully!");
            }
        }
        model.addAttribute("entryUrl", Routes.CUSTOMER_ENTRY);
        return "customer/customer-entry";
    }

    @PreAuthorize("hasAuthority('Admin')")
    @PostMapping(Routes.CUSTOMER_ENTRY)
    public String createCustomer(Customer customer, Model model) {
        boolean flag = customer.getId() == null;
        try {
            customer = service.createCustomer(customer);
            return "redirect:" + Routes.CUSTOMER_ENTRY + "?id=" + customer.getId() + "&msg=" + (flag ? "create" : "update");
        } catch (Exception e) {
            if (flag) {
                SubmitResult.error(model, "Customer could not be created!");
            } else {
                SubmitResult.error(model, "Customer could not be updated!");
            }
        }
        model.addAttribute("customer", customer);
        model.addAttribute("entryUrl", Routes.CUSTOMER_ENTRY);
        return "customer/customer-entry";
    }

    @GetMapping(Routes.CUSTOMER_LIST)
    public String customerList(Model model) {
        model.addAttribute("dataUrl", Routes.CUSTOMER_SEARCH);
        model.addAttribute("openUrl", Routes.CUSTOMER_ENTRY);
        return "customer/customer-list";
    }

    @GetMapping(Routes.CUSTOMER_SEARCH)
    @ResponseBody
    public ResponseEntity<List<Customer>> searchCustomer() {
        return new ResponseEntity<>(service.getCustomerList(), HttpStatus.OK);
    }

    @GetMapping(Routes.CUSTOMER_SEARCH_BY_NAME_OR_PHONE)
    @ResponseBody
    public ResponseEntity<List<Customer>> searchCustomerByNameOrPhone(@RequestParam(name = "nameOrPhone") String nameOrPhone) {
        return new ResponseEntity<>(service.getCustomerListByNameOrPhone(nameOrPhone), HttpStatus.OK);
    }
}
