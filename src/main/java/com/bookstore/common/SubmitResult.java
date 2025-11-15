package com.bookstore.common;

import org.springframework.ui.Model;

public class SubmitResult {
    public static void success(Model model, String message) {
        model.addAttribute("message", message);
        model.addAttribute("success", true);
        model.addAttribute("submitted", true);
    }

    public static void error(Model model, String message) {
        model.addAttribute("message", message);
        model.addAttribute("success", false);
        model.addAttribute("submitted", true);
    }
}
