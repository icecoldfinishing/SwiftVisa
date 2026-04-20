package com.visa.management.controller.demandeur;

import com.visa.management.model.demandeur.Demandeur;
import com.visa.management.service.demandeur.DemandeurService;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/demandeurs")
public class DemandeurController {

    private final DemandeurService demandeurService;

    public DemandeurController(DemandeurService demandeurService) {
        this.demandeurService = demandeurService;
    }

    @GetMapping
    public List<Demandeur> listDemandeurs() {
        return demandeurService.findAllDemandeurs();
    }
}
