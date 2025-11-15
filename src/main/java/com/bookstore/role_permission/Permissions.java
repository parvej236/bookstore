package com.bookstore.role_permission;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Permissions {
    /*Admission*/
    DASHBOARD_VIEW("Can View Dashboard"),

    SALES_VIEW("Can View Sales"),
    SALES_CREATE("Can Create Sales"),
    SALES_SUBMIT("Can Submit Sales"),

    STOCK_IN_VIEW("Can View Stock In"),
    STOCK_IN_CREATE("Can Create Stock In"),
    STOCK_IN_SUBMIT("Can Submit Stock In"),
    STOCK_IN_APPROVE("Can Approve Stock In"),

    BOOK_VIEW("Can View Book"),
    BOOK_CREATE("Can Create Book"),

    SUPPLIER_VIEW("Can View Supplier"),
    SUPPLIER_CREATE("Can Create Supplier"),

    CUSTOMER_VIEW("Can View Customer"),
    CUSTOMER_CREATE("Can Create Customer"),

    USER_VIEW("Can View User"),
    USER_CREATE("Can Create User"),

    ROLE_VIEW("Can View Role"),
    ROLE_CREATE("Can Create Role"),

    ROLE_PERMISSION_VIEW("Can View Role Permission"),
    ROLE_PERMISSION_CREATE("Can Create Role Permission Create"),

    REPORT_GENERATE("Can Generate Report");
    private final String title;
}
