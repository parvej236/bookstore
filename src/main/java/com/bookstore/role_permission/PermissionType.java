package com.bookstore.role_permission;

public enum PermissionType {
    VIEW("View"),
    CREATE("Create"),
    UPDATE("Update"),
    SUBMIT("Submit"),
    APPROVE("Approve");

    private final String title;
    PermissionType(String title) { this.title = title; }
    public String getTitle() { return title; }
}

