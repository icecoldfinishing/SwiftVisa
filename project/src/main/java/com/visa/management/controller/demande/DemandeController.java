package com.visa.management.controller.demande;

import com.visa.management.controller.demande.dto.CreateDemandeRequest;
import com.visa.management.controller.demande.dto.DemandeCreatedResponse;
import com.visa.management.controller.demande.dto.DemandeListResponse;
import com.visa.management.model.demande.Demande;
import com.visa.management.service.demande.DemandeService;
import jakarta.validation.Valid;
import java.time.LocalDate;
import java.util.List;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/demandes")
public class DemandeController {

    private final DemandeService demandeService;

    public DemandeController(DemandeService demandeService) {
        this.demandeService = demandeService;
    }

    @GetMapping("/all")
    public List<Demande> listDemandes() {
        return demandeService.findAllDemandes();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public DemandeCreatedResponse createDemande(@Valid @RequestBody CreateDemandeRequest request) {
        return demandeService.createDemande(request);
    }

    @GetMapping
    public List<DemandeListResponse> searchDemandes(
        @RequestParam(required = false) String statut,
        @RequestParam(required = false) Long idCategorieVisa,
        @RequestParam(required = false) Long idNationalite,
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateDebut,
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateFin,
        @RequestParam(required = false) String recherche
    ) {
        return demandeService.searchDemandes(statut, idCategorieVisa, idNationalite, dateDebut, dateFin, recherche);
    }
}
