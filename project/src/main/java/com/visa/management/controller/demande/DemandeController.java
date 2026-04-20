package com.visa.management.controller.demande;

import com.visa.management.model.demande.Demande;
import com.visa.management.service.demande.DemandeService;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/demandes")
public class DemandeController {

    private final DemandeService demandeService;

    public DemandeController(DemandeService demandeService) {
        this.demandeService = demandeService;
    }

    @GetMapping
    public List<Demande> listDemandes() {
        return demandeService.findAllDemandes();
    }
}
