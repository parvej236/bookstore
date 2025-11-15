package com.bookstore.role;

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
public class RoleController {
    private final RoleService roleService;

    @GetMapping(Routes.ROLE_ENTRY)
    public String roleEntry(@RequestParam(name="id", required = false) Long id, @RequestParam(name = "msg", required = false) String msg, Model model){
        Role role = new Role();
        if(id != null){
            role = roleService.findById(id);
            if (msg != null && msg.equals("create")) {
                SubmitResult.success(model, "Role created successfully!");
            }
            if (msg != null && msg.equals("update")) {
                SubmitResult.success(model, "Role updated successfully!");
            }
        }
        model.addAttribute("role", role);
        model.addAttribute("entryUrl", Routes.ROLE_ENTRY);
        return "role/role-entry";
    }

    @PreAuthorize("hasAuthority('Admin')")
    @PostMapping(Routes.ROLE_ENTRY)
    public String createRole(Role role, Model model) {
        boolean flag = role.getId() == null;
        try {
            role = roleService.save(role);
            return "redirect:" + Routes.ROLE_ENTRY + "?id=" + role.getId() + "&msg=" + (flag ? "create" : "update");
        } catch (Exception e) {
            if (flag) {
                SubmitResult.error(model, "Role could not be created!");
            } else {
                SubmitResult.error(model, "Role could not be updated!");
            }
        }
        model.addAttribute("role", role);
        model.addAttribute("entryUrl", Routes.ROLE_ENTRY);
        return "role/role-entry";
    }

    @GetMapping(Routes.ROLE_LIST)
    public String roleList(Model model) {
        model.addAttribute("dataUrl", Routes.ROLE_SEARCH);
        model.addAttribute("openUrl",  Routes.ROLE_ENTRY);
        return "role/role-list";
    }

    @GetMapping(Routes.ROLE_SEARCH)
    @ResponseBody
    public ResponseEntity<List<Role>> searchRole() {
        return new ResponseEntity<>(roleService.findAll(), HttpStatus.OK);
    }





}
