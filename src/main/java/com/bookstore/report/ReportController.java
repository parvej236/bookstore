package com.bookstore.report;

import com.bookstore.common.Routes;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ReportController {
    @GetMapping(Routes.REPORT)
    public String supplierEntry() {
        return "report/report";
    }
}
