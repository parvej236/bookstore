package com.bookstore.supplier;

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
public class SupplierController {
    private final SupplierService service;

    @GetMapping(Routes.SUPPLIER_ENTRY)
    public String supplierEntry(@RequestParam(name = "id", required = false) Long id, @RequestParam(name = "msg", required = false) String msg, Model model) {
        if (id == null) {
            model.addAttribute("supplier", new Supplier());
        } else {
            model.addAttribute("supplier", service.getById(id));
            if (msg != null && msg.equals("create")) {
                SubmitResult.success(model, "Supplier created successfully!");
            }
            if (msg != null && msg.equals("update")) {
                SubmitResult.success(model, "Supplier updated successfully!");
            }
        }
        model.addAttribute("entryUrl", Routes.SUPPLIER_ENTRY);
        return "supplier/supplier-entry";
    }

    @PreAuthorize("hasAuthority('Admin')")
    @PostMapping(Routes.SUPPLIER_ENTRY)
    public String supplierEntry(Model model, Supplier supplier) {
        boolean flag = supplier.getId() == null;
        try {
            supplier = service.createSupplier(supplier);
            return "redirect:" + Routes.SUPPLIER_ENTRY + "?id=" + supplier.getId() + "&msg=" + (flag ? "create" : "update");
        } catch (Exception e) {
            if (flag) {
                SubmitResult.error(model, "Supplier could not be created!");
            } else {
                SubmitResult.error(model, "Supplier could not be updated!");
            }
        }
        model.addAttribute("supplier", supplier);
        model.addAttribute("entryUrl", Routes.SUPPLIER_ENTRY);
        return "supplier/supplier-entry";
    }

    @GetMapping(Routes.SUPPLIER_LIST)
    public String supplierList(Model model) {
        model.addAttribute("dataUrl", Routes.SUPPLIER_SEARCH);
        model.addAttribute("openUrl", Routes.SUPPLIER_ENTRY);
        return "supplier/supplier-list";
    }

    @GetMapping(Routes.SUPPLIER_SEARCH)
    @ResponseBody
    public ResponseEntity<List<Supplier>> supplierSearch() {
        return new ResponseEntity<>(service.getSupplierList(), HttpStatus.OK);
    }
}
