package com.visa.management.controller.passport;

import com.visa.management.model.passport.Passport;
import com.visa.management.service.passport.PassportService;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/passports")
public class PassportApiController {

    private final PassportService passportService;

    public PassportApiController(PassportService passportService) {
        this.passportService = passportService;
    }

    @GetMapping
    public List<Passport> listPassports() {
        return passportService.findAllPassports();
    }
}
