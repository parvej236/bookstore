package com.bookstore.role_permission;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum Modules {
    DASHBOARD("Dashboard"),
    SALES("Sales"),
    STOCK("Stock"),
    STOCK_IN("Stock In"),
    BOOK("Book"),
    SUPPLIER("Supplier"),
    CUSTOMER("Customer"),
    USER("User"),
    ROLE("Role"),
    ROLE_PERMISSION("Role Permission"),
    REPORT("Report");

    @NonNull
    private final String title;
}
