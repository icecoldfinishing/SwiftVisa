package com.visa.management.service.demande;

import java.time.LocalDate;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.visa.management.controller.demande.dto.CreateDemandeRequest;
import com.visa.management.controller.demande.dto.DemandeCreatedResponse;
import com.visa.management.controller.demande.dto.DemandeDocumentScanItemResponse;
import com.visa.management.controller.demande.dto.DemandeListResponse;
import com.visa.management.model.demande.Demande;

public interface DemandeService {

    List<Demande> findAllDemandes();

    DemandeCreatedResponse createDemande(CreateDemandeRequest request);

    DemandeCreatedResponse updateDemande(Long idDemande, CreateDemandeRequest request);

    DemandeCreatedResponse validateDemande(Long idDemande);

    CreateDemandeRequest getDemandeForEdit(Long idDemande);

    List<DemandeListResponse> searchDemandes(
        String statut,
        Long idCategorieVisa,
        Long idNationalite,
        LocalDate dateDebut,
        LocalDate dateFin,
        String recherche
    );

    List<DemandeDocumentScanItemResponse> getDemandeDocumentsForScan(Long idDemande);

    void uploadDemandeDocumentScan(Long idDemande, Long idDemandeDocument, MultipartFile file);

    DemandeCreatedResponse finishDemandeScan(Long idDemande);
}
