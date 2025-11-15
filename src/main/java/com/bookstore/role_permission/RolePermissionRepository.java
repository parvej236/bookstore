package com.bookstore.role_permission;

import com.bookstore.role.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RolePermissionRepository extends JpaRepository<RolePermission, Long> {
    List<RolePermission> findByRole(Role role);
    boolean existsByRoleAndPermission(Role role, Permissions permission);
}
