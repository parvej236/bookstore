package com.bookstore.common;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class GlobalErrorController implements ErrorController {
    @RequestMapping(Routes.ERROR)
    public String somethingWrong() {
        return "common/something-wrong";
    }
}
