package com.bookstore.user;

import com.bookstore.common.Routes;
import com.bookstore.common.SubmitResult;
import com.bookstore.role.RoleService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.concurrent.ExecutionException;

@Controller
@AllArgsConstructor
@PreAuthorize("hasAuthority('Admin')")
public class UserController {
    private final UserService service;
    private final RoleService roleService;

    @GetMapping(Routes.USER_ENTRY)
    public String userEntry(@RequestParam(name = "id", required = false) Long id, @RequestParam(name = "msg", required = false) String msg, Model model) throws ExecutionException, InterruptedException {
        if (id == null) {
            model.addAttribute("user", new User());
            model.addAttribute("roles", roleService.findAll());
        } else {
            model.addAttribute("user", service.getById(id));
            model.addAttribute("roles", roleService.findAll());
            if (msg != null && msg.equals("create")) {
                SubmitResult.success(model, "User created successfully!");
            }
            if (msg != null && msg.equals("update")) {
                SubmitResult.success(model, "User updated successfully!");
            }
        }
        model.addAttribute("entryUrl", Routes.USER_ENTRY);
        return "user/user-entry";
    }

    @PostMapping(Routes.USER_ENTRY)
    public String createUser(User user, Model model, BindingResult result) {
        boolean flag = user.getId() == null;
        service.validateUser(user, result);
        try {
            if (!result.hasErrors()) {
                user = service.createUser(user);
                return "redirect:" + Routes.USER_ENTRY + "?id=" + user.getId() + "&msg=" + (flag ? "create" : "update");
            } else {
                SubmitResult.error(model, "User could not be " + (flag ? "created" : "updated") + "!");
            }
        } catch (Exception e) {
            SubmitResult.error(model, "User could not be " + (flag ? "created" : "updated") + "!");
        }
        model.addAttribute("user", user);
        model.addAttribute("roles", roleService.findAll());
        model.addAttribute("entryUrl", Routes.USER_ENTRY);
        return "user/user-entry";
    }

    @GetMapping(Routes.USER_LIST)
    public String userList(Model model) {
        model.addAttribute("dataUrl", Routes.USER_SEARCH);
        model.addAttribute("openUrl", Routes.USER_ENTRY);
        return "user/user-list";
    }

    @GetMapping(Routes.USER_SEARCH)
    @ResponseBody
    public ResponseEntity<List<User>> searchUser() throws ExecutionException, InterruptedException {
        return new ResponseEntity<>(service.getUserList(), HttpStatus.OK);
    }
}
