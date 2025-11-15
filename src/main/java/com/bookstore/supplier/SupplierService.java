package com.bookstore.supplier;

import com.bookstore.config.MailConfig;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class SupplierService {
    private final MailConfig mailConfig;
    private final SupplierRepository repository;

    public Supplier createSupplier(Supplier supplier) {
        if (supplier.getId() == null) {
            supplier = repository.save(supplier);
            mailConfig.supplierCreateMail(supplier);
            return supplier;
        } else {
            return repository.save(supplier);
        }
    }

    public Supplier getById(Long id) {
        return repository.findById(id).orElse(null);
    }

    public List<Supplier> getSupplierList() {
        return repository.findAll();
    }
}
