package com.bookstore.role_permission;

import com.bookstore.common.Routes;
import com.bookstore.common.SubmitResult;
import com.bookstore.role.Role;
import com.bookstore.role.RoleService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@AllArgsConstructor
public class RolePermissionController {

    private final RoleService roleService;
    private final RolePermissionService rpService;

    @GetMapping(Routes.ROLE_PERMISSION_ENTRY)
    public String rolePermissionEntry(@RequestParam(name="id", required = false) Long roleId, @RequestParam(name = "msg", required = false) String msg, Model model) {
        List<Role> roles = roleService.findAll();
        model.addAttribute("roles", roles);
        model.addAttribute("modules", Modules.values());
        model.addAttribute("allPermissions", Permissions.values());
        model.addAttribute("entryUrl", Routes.ROLE_PERMISSION_ENTRY);

        // Load existing permissions if roleId is provided
        if (roleId != null) {
            Role selectedRole = roleService.findById(roleId);
            if (selectedRole != null) {
                model.addAttribute("selectedRoleId", roleId);
                List<RolePermission> existingPermissions = rpService.findAllByRoleId(roleId);
                List<String> permissionNames = existingPermissions.stream()
                        .map(rp -> rp.getPermission().name())
                        .collect(Collectors.toList());
                model.addAttribute("existingPermissions", permissionNames);
            }
        }

        if (msg != null && msg.equals("success")) {
            SubmitResult.success(model, "Role Permissions saved successfully!");
        } else if (msg != null && msg.equals("error")) {
            SubmitResult.error(model, "Failed to save Role Permissions!");
        }

        return "role-permission/role-permission-entry";
    }

    @PostMapping(Routes.ROLE_PERMISSION_ENTRY)
    public String saveRolePermissions(@RequestParam("roleId") Long roleId, 
                                     @RequestParam(value = "permissions", required = false) List<String> permissionNames, 
                                     Model model) {
        try {
            Role role = roleService.findById(roleId);
            if (role == null) {
                SubmitResult.error(model, "Role not found!");
                return rolePermissionEntry(null, "error", model);
            }

            List<Permissions> permissions = new ArrayList<>();
            if (permissionNames != null && !permissionNames.isEmpty()) {
                permissions = permissionNames.stream()
                        .map(Permissions::valueOf)
                        .collect(Collectors.toList());
            }

            rpService.savePermissions(role, permissions);
            return "redirect:" + Routes.ROLE_PERMISSION_ENTRY + "?id=" + roleId + "&msg=success";
        } catch (Exception e) {
            SubmitResult.error(model, "Role Permissions could not be saved: " + e.getMessage());
            return rolePermissionEntry(roleId, "error", model);
        }
    }

}
