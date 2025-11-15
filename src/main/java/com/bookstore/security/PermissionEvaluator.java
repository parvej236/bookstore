package com.bookstore.security;

import com.bookstore.role.Role;
import com.bookstore.role_permission.Permissions;
import com.bookstore.role_permission.RolePermissionService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class PermissionEvaluator {

    private final RolePermissionService rpService;

    public boolean hasPermission(Authentication auth, String permissionName) {
        if (auth == null || permissionName == null) return false;

        Permissions p = Permissions.valueOf(permissionName);

        Role role = (Role) auth.getAuthorities().iterator().next();  // or fetch from DB

        return rpService.roleHasPermission(role, p);
    }
}
