package com.bookstore.role_permission;

import com.bookstore.common.Routes;
import com.bookstore.common.SubmitResult;
import com.bookstore.role.Role;
import com.bookstore.role.RoleService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@AllArgsConstructor
public class RolePermissionController {

    private final RoleService roleService;
    private final RolePermissionService rpService;

    @GetMapping(Routes.ROLE_PERMISSION_ENTRY)
    public String rolePermissionEntry(@RequestParam(name="id", required = false) Long id,@RequestParam(name = "msg", required = false) String msg, Model model) {
        RolePermission rolePermission = new RolePermission();

        if(id != null) {
//            rolePermission = rpService.findById(id);
            if (msg != null && msg.equals("create")) {
                SubmitResult.success(model, "Role Permission created successfully!");
            }
            if (msg != null && msg.equals("update")) {
                SubmitResult.success(model, "Role Permission updated successfully!");
            }
        }
        List<Role> roles = roleService.findAll();

        model.addAttribute("roles", roles);
        model.addAttribute("modules", Modules.values());
        model.addAttribute("permissions", Permissions.values());
        model.addAttribute("rolePermission", rolePermission);
        model.addAttribute("entryUrl", Routes.ROLE_PERMISSION_ENTRY);
        return "role-permission/role-permission-entry";
    }

    @PostMapping(Routes.ROLE_PERMISSION_ENTRY)
    public String saveRolePermissions(@ModelAttribute RolePermission rolePermission) {
        rpService.save(rolePermission);
        return "redirect:" + Routes.ROLE_PERMISSION_LIST;
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
