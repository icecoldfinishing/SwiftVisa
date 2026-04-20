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

    @GetMapping("/")
    public String dashboard(Model model) {
        addLayoutContext(model, "dashboard");
        model.addAttribute("passportCount", passportService.findAllPassports().size());
        return "passport/dashboard";
    }

    @GetMapping("/passports")
    public String listPassports(Model model) {
        addLayoutContext(model, "passports");
        model.addAttribute("passports", passportService.findAllPassports());
        return "passport/list";
    }

    private void addLayoutContext(Model model, String activeMenu) {
        model.addAttribute("accountName", "Compte SwiftVisa");
        model.addAttribute("accountRole", "AGENT");
        model.addAttribute("activeMenu", activeMenu);
    }
}
