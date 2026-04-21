package com.visa.management.controller.visa;

import com.visa.management.model.visa.VisaCategorie;
import com.visa.management.service.visa.VisaCategorieService;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/visa-categories")
public class VisaCategorieController {

    private final VisaCategorieService visaCategorieService;

    public VisaCategorieController(VisaCategorieService visaCategorieService) {
        this.visaCategorieService = visaCategorieService;
    }

    @GetMapping
    public List<VisaCategorie> listVisaCategories() {
        return visaCategorieService.findAllVisaCategories();
    }
}
