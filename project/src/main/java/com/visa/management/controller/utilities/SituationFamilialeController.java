package com.visa.management.controller.utilities;

import com.visa.management.model.utilities.SituationFamiliale;
import com.visa.management.service.utilities.SituationFamilialeService;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/situations-familiales")
public class SituationFamilialeController {

    private final SituationFamilialeService situationFamilialeService;

    public SituationFamilialeController(SituationFamilialeService situationFamilialeService) {
        this.situationFamilialeService = situationFamilialeService;
    }

    @GetMapping
    public List<SituationFamiliale> listSituationsFamiliales() {
        return situationFamilialeService.findAllSituationsFamiliales();
    }
}
