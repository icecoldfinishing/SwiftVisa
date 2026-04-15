package com.visa.management.controller.passport;

import com.visa.management.service.passport.PassportService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PassportController {

    private final PassportService passportService;

    public PassportController(PassportService passportService) {
        this.passportService = passportService;
    }

    @GetMapping({"/", "/passports"})
    public String listPassports(Model model) {
        model.addAttribute("passports", passportService.findAllPassports());
        return "passport/list";
    }
}
