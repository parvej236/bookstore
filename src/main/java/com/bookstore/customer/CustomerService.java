package com.bookstore.customer;

import com.bookstore.config.MailConfig;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class CustomerService {
    private final MailConfig mailConfig;
    private final CustomerRepository repository;

    public Customer createCustomer(Customer customer) {
        if (customer.getId() == null) {
            customer = repository.save(customer);
            mailConfig.customerCreateMail(customer);
        } else {
            customer = repository.save(customer);
        }
        return customer;
    }

    public Customer getById(Long id) {
        return repository.findById(id).orElse(null);
    }

    public List<Customer> getCustomerList() {
        return repository.findAll();
    }

    public List<Customer> getCustomerListByNameOrPhone(String nameOrPhone) {
        return repository.findAllByNameContainingIgnoreCaseOrPhoneContainingIgnoreCase(nameOrPhone, nameOrPhone);
    }

    public int getTotalCount() {
        return repository.getTotalCount();
    }
}
