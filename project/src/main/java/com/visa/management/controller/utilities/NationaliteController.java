package com.visa.management.controller.utilities;

import com.visa.management.model.utilities.Nationalite;
import com.visa.management.service.utilities.NationaliteService;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/nationalites")
public class NationaliteController {

    private final NationaliteService nationaliteService;

    public NationaliteController(NationaliteService nationaliteService) {
        this.nationaliteService = nationaliteService;
    }

    @GetMapping
    public List<Nationalite> listNationalites() {
        return nationaliteService.findAllNationalites();
    }
}
