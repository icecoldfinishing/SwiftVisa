package com.visa.management.controller.demande;

import com.visa.management.model.demande.DemandeTypeDonnees;
import com.visa.management.service.demande.DemandeTypeDonneesService;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/demande-type-donnees")
public class DemandeTypeDonneesController {

    private final DemandeTypeDonneesService demandeTypeDonneesService;

    public DemandeTypeDonneesController(DemandeTypeDonneesService demandeTypeDonneesService) {
        this.demandeTypeDonneesService = demandeTypeDonneesService;
    }

    @GetMapping
    public List<DemandeTypeDonnees> listDemandeTypeDonnees() {
        return demandeTypeDonneesService.findAllDemandeTypeDonnees();
    }
}