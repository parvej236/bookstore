package com.bookstore.role_permission;

import com.bookstore.common.Routes;
import com.bookstore.role.Role;
import com.bookstore.role.RoleService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@AllArgsConstructor
public class RolePermissionController {

    private final RoleService roleService;
    private final RolePermissionService rpService;

    @GetMapping(Routes.ROLE_PERMISSION_ENTRY)
    public String rolePermissionEntry(@RequestParam(required = false) Long roleId, Model model) {
        RolePermission rolePermission = new RolePermission();
        List<Role> roles = roleService.findAll();
//        Role selectedRole = null;
//        List<RolePermission> selectedPermissions = List.of();

//        if (roleId != null) {
//            selectedRole = roleService.findById(roleId);
//            selectedPermissions = rpService.getPermissions(selectedRole);
//        }

        model.addAttribute("roles", roles);
//        model.addAttribute("selectedRole", selectedRole);
        model.addAttribute("modules", Modules.values());
        model.addAttribute("permissions", Permissions.values());
        model.addAttribute("rolePermission", rolePermission);
//        model.addAttribute("selected", selectedPermissions);
        model.addAttribute("entryUrl", Routes.ROLE_PERMISSION_ENTRY);

        return "role-permission/role-permission-entry";
    }

    @PostMapping(Routes.ROLE_PERMISSION_ENTRY)
    public String saveRolePermissions(@RequestParam Long roleId,
                                      @RequestParam(name = "permissions", required = false) List<String> permissionStrings, RolePermission rolePermission) {

        Role role = roleService.findById(roleId);

        List<Permissions> permissions = List.of();
        if (permissionStrings != null && !permissionStrings.isEmpty()) {
            permissions = permissionStrings.stream()
                    .map(Permissions::valueOf)
                    .toList();
        }

        rpService.savePermissions(role, permissions);

        return "redirect:" + Routes.ROLE_PERMISSION_ENTRY + "?roleId=" + roleId + "&msg=success";
    }

    @GetMapping(Routes.ROLE_PERMISSION_LIST)
    public String rolePermissionList(Model model) {
        model.addAttribute("dataUrl", Routes.ROLE_PERMISSION_SEARCH);
        model.addAttribute("openUrl", Routes.ROLE_PERMISSION_ENTRY);
        return "role-permission/role-permission-list";
    }

    @GetMapping(Routes.ROLE_PERMISSION_SEARCH)
    @ResponseBody
    public ResponseEntity<List<RolePermission>> searchRolePermissions() {
        return new ResponseEntity<>(rpService.getAll(), HttpStatus.OK);
    }


}
