package com.visa.management.service.demande;

import com.visa.management.model.demande.Demande;
import com.visa.management.controller.demande.dto.CreateDemandeRequest;
import com.visa.management.controller.demande.dto.DemandeCreatedResponse;
import com.visa.management.controller.demande.dto.DemandeListResponse;
import java.time.LocalDate;
import java.util.List;

public interface DemandeService {

    List<Demande> findAllDemandes();

    DemandeCreatedResponse createDemande(CreateDemandeRequest request);

    DemandeCreatedResponse updateDemande(Long idDemande, CreateDemandeRequest request);

    CreateDemandeRequest getDemandeForEdit(Long idDemande);

    List<DemandeListResponse> searchDemandes(
        String statut,
        Long idCategorieVisa,
        Long idNationalite,
        LocalDate dateDebut,
        LocalDate dateFin,
        String recherche
    );
}
