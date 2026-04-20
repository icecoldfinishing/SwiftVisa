package com.visa.management.controller.demande;

import com.visa.management.model.demande.DemandeType;
import com.visa.management.service.demande.DemandeTypeService;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/demande-types")
public class DemandeTypeController {

    private final DemandeTypeService demandeTypeService;

    public DemandeTypeController(DemandeTypeService demandeTypeService) {
        this.demandeTypeService = demandeTypeService;
    }

    @GetMapping
    public List<DemandeType> listDemandeTypes() {
        return demandeTypeService.findAllDemandeTypes();
    }
}
