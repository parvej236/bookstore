package com.bookstore.role_permission;

import com.bookstore.role.Role;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class RolePermissionService {

    private final RolePermissionRepository repo;

    public List<RolePermission> getPermissions(Role role) {
        return repo.findByRole(role);
    }

    public void savePermissions(Role role, List<Permissions> permissions) {
        // Remove old permissions
        repo.findByRole(role).forEach(repo::delete);

        // Add new permissions
        for (Permissions p : permissions) {
            RolePermission rp = new RolePermission();
            rp.setRole(role);
            rp.setPermission(p);
            repo.save(rp);
        }
    }

    public boolean roleHasPermission(Role role, Permissions permission) {
        return repo.existsByRoleAndPermission(role, permission);
    }

    public List<RolePermission> getAll() {
        return repo.findAll();
    }

}
