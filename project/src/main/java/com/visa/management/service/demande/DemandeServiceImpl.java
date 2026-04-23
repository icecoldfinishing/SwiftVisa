package com.visa.management.service.demande;

import com.visa.management.controller.demande.dto.CreateDemandeRequest;
import com.visa.management.controller.demande.dto.DemandeCreatedResponse;
import com.visa.management.controller.demande.dto.DemandeListResponse;
import com.visa.management.model.demande.Demande;
import com.visa.management.model.demande.DemandeDocument;
import com.visa.management.model.demande.DemandeStatus;
import com.visa.management.model.demande.DemandeTypeStatus;
import com.visa.management.model.demandeur.Demandeur;
import com.visa.management.model.passport.Passport;
import com.visa.management.repository.demande.DemandeDocumentRepository;
import com.visa.management.repository.demande.DemandeRepository;
import com.visa.management.repository.demande.DemandeStatusRepository;
import com.visa.management.repository.demande.DemandeTypeStatusRepository;
import com.visa.management.repository.demande.projection.DemandeSearchProjection;
import com.visa.management.repository.documents.DocumentCategorieVisaRepository;
import com.visa.management.repository.demandeur.DemandeurRepository;
import com.visa.management.repository.passport.PassportRepository;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class DemandeServiceImpl implements DemandeService {

    private static final String DOSSIER_CREE = "DOSSIER_CREE";

    private final DemandeRepository demandeRepository;
    private final DemandeurRepository demandeurRepository;
    private final PassportRepository passportRepository;
    private final DemandeTypeStatusRepository demandeTypeStatusRepository;
    private final DemandeStatusRepository demandeStatusRepository;
    private final DocumentCategorieVisaRepository documentCategorieVisaRepository;
    private final DemandeDocumentRepository demandeDocumentRepository;

    public DemandeServiceImpl(
        DemandeRepository demandeRepository,
        DemandeurRepository demandeurRepository,
        PassportRepository passportRepository,
        DemandeTypeStatusRepository demandeTypeStatusRepository,
        DemandeStatusRepository demandeStatusRepository,
        DocumentCategorieVisaRepository documentCategorieVisaRepository,
        DemandeDocumentRepository demandeDocumentRepository
    ) {
        this.demandeRepository = demandeRepository;
        this.demandeurRepository = demandeurRepository;
        this.passportRepository = passportRepository;
        this.demandeTypeStatusRepository = demandeTypeStatusRepository;
        this.demandeStatusRepository = demandeStatusRepository;
        this.documentCategorieVisaRepository = documentCategorieVisaRepository;
        this.demandeDocumentRepository = demandeDocumentRepository;
    }

    @Override
    public List<Demande> findAllDemandes() {
        return demandeRepository.findAll();
    }

    @Override
    @Transactional
    public DemandeCreatedResponse createDemande(CreateDemandeRequest request) {
        validateRequiredDocuments(request.getIdCategorieVisa(), request.getUploadedDocumentIds());

        Demandeur demandeur = new Demandeur();
        demandeur.setNom(request.getEtatCivil().getNom());
        demandeur.setPrenom(request.getEtatCivil().getPrenom());
        demandeur.setNomJeuneFille(request.getEtatCivil().getNomJeuneFille());
        demandeur.setDateNaissance(request.getEtatCivil().getDateNaissance());
        demandeur.setLieuNaissance(request.getEtatCivil().getLieuNaissance());
        demandeur.setIdSituationFamiliale(request.getEtatCivil().getSituationFamiliale());
        demandeur.setIdNationalite(request.getEtatCivil().getNationalite());
        demandeur.setTelephone(request.getEtatCivil().getContact());
        demandeur.setAdresse(request.getEtatCivil().getAdresse());
        Demandeur savedDemandeur = demandeurRepository.save(demandeur);

        Passport passport = new Passport();
        passport.setIdDemandeur(savedDemandeur.getId());
        passport.setNumero(request.getPassport().getNumero());
        passport.setDateDelivrance(request.getPassport().getDateDelivrance());
        passport.setDateExpiration(request.getPassport().getDateExpiration());
        Passport savedPassport = passportRepository.save(passport);

        Demande demande = new Demande();
        demande.setIdDemandeur(savedDemandeur.getId());
        demande.setIdCategorieVisa(request.getIdCategorieVisa());
        demande.setIdPassport(savedPassport.getId());
        demande.setDateDemande(LocalDate.now());
        demande.setLieu(request.getVisa().getLieu());
        demande.setDateEntree(request.getVisa().getDateEntree());
        demande.setDateExpiration(request.getVisa().getDateExpiration());
        demande.setMotifDemande(request.getVisa().getMotifDemande());
        Demande savedDemande = demandeRepository.save(demande);

        Set<Long> uploadedDocuments = new HashSet<>(request.getUploadedDocumentIds());
        for (Long documentId : uploadedDocuments) {
            DemandeDocument demandeDocument = new DemandeDocument();
            demandeDocument.setIdDemande(savedDemande.getId());
            demandeDocument.setIdDocument(documentId);
            demandeDocumentRepository.save(demandeDocument);
        }

        DemandeTypeStatus dossierCreeStatus = demandeTypeStatusRepository
            .findByCodeIgnoreCase(DOSSIER_CREE)
            .orElseGet(() -> {
                DemandeTypeStatus status = new DemandeTypeStatus();
                status.setCode(DOSSIER_CREE);
                status.setLibelle("Dossier cree");
                return demandeTypeStatusRepository.save(status);
            });

        DemandeStatus demandeStatus = new DemandeStatus();
        demandeStatus.setIdDemande(savedDemande.getId());
        demandeStatus.setIdDemandeTypeStatus(dossierCreeStatus.getId());
        demandeStatusRepository.save(demandeStatus);

        return new DemandeCreatedResponse(savedDemande.getId(), DOSSIER_CREE);
    }

    @Override
    @Transactional
    public DemandeCreatedResponse updateDemande(Long idDemande, CreateDemandeRequest request) {
        validateRequiredDocuments(request.getIdCategorieVisa(), request.getUploadedDocumentIds());

        Demande demande = demandeRepository
            .findById(idDemande)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Dossier introuvable"));

        Demandeur demandeur = demandeurRepository
            .findById(demande.getIdDemandeur())
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Demandeur introuvable"));

        Passport passport = passportRepository
            .findById(demande.getIdPassport())
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Passport introuvable"));

        demandeur.setNom(request.getEtatCivil().getNom());
        demandeur.setPrenom(request.getEtatCivil().getPrenom());
        demandeur.setNomJeuneFille(request.getEtatCivil().getNomJeuneFille());
        demandeur.setDateNaissance(request.getEtatCivil().getDateNaissance());
        demandeur.setLieuNaissance(request.getEtatCivil().getLieuNaissance());
        demandeur.setIdSituationFamiliale(request.getEtatCivil().getSituationFamiliale());
        demandeur.setIdNationalite(request.getEtatCivil().getNationalite());
        demandeur.setTelephone(request.getEtatCivil().getContact());
        demandeur.setAdresse(request.getEtatCivil().getAdresse());
        demandeurRepository.save(demandeur);

        passport.setNumero(request.getPassport().getNumero());
        passport.setDateDelivrance(request.getPassport().getDateDelivrance());
        passport.setDateExpiration(request.getPassport().getDateExpiration());
        passportRepository.save(passport);

        demande.setIdCategorieVisa(request.getIdCategorieVisa());
        demande.setLieu(request.getVisa().getLieu());
        demande.setDateEntree(request.getVisa().getDateEntree());
        demande.setDateExpiration(request.getVisa().getDateExpiration());
        demande.setMotifDemande(request.getVisa().getMotifDemande());
        demandeRepository.save(demande);

        demandeDocumentRepository.deleteAllByIdDemande(idDemande);
        demandeDocumentRepository.flush();
        Set<Long> uploadedDocuments = new HashSet<>(request.getUploadedDocumentIds());
        for (Long documentId : uploadedDocuments) {
            DemandeDocument demandeDocument = new DemandeDocument();
            demandeDocument.setIdDemande(idDemande);
            demandeDocument.setIdDocument(documentId);
            demandeDocumentRepository.save(demandeDocument);
        }

        return new DemandeCreatedResponse(idDemande, DOSSIER_CREE);
    }

    @Override
    @Transactional(readOnly = true)
    public CreateDemandeRequest getDemandeForEdit(Long idDemande) {
        Demande demande = demandeRepository
            .findById(idDemande)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Dossier introuvable"));

        Demandeur demandeur = demandeurRepository
            .findById(demande.getIdDemandeur())
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Demandeur introuvable"));

        Passport passport = passportRepository
            .findById(demande.getIdPassport())
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Passport introuvable"));

        List<Long> uploadedDocumentIds = demandeDocumentRepository
            .findByIdDemande(idDemande)
            .stream()
            .map(DemandeDocument::getIdDocument)
            .collect(Collectors.toList());

        CreateDemandeRequest request = new CreateDemandeRequest();
        request.setIdCategorieVisa(demande.getIdCategorieVisa());
        request.setUploadedDocumentIds(uploadedDocumentIds);

        CreateDemandeRequest.EtatCivilRequest etatCivil = new CreateDemandeRequest.EtatCivilRequest();
        etatCivil.setNom(demandeur.getNom());
        etatCivil.setPrenom(demandeur.getPrenom());
        etatCivil.setNomJeuneFille(demandeur.getNomJeuneFille());
        etatCivil.setDateNaissance(demandeur.getDateNaissance());
        etatCivil.setLieuNaissance(demandeur.getLieuNaissance());
        etatCivil.setSituationFamiliale(demandeur.getIdSituationFamiliale());
        etatCivil.setNationalite(demandeur.getIdNationalite());
        etatCivil.setContact(demandeur.getTelephone());
        etatCivil.setAdresse(demandeur.getAdresse());
        request.setEtatCivil(etatCivil);

        CreateDemandeRequest.PassportRequest passportRequest = new CreateDemandeRequest.PassportRequest();
        passportRequest.setNumero(passport.getNumero());
        passportRequest.setDateDelivrance(passport.getDateDelivrance());
        passportRequest.setDateExpiration(passport.getDateExpiration());
        request.setPassport(passportRequest);

        CreateDemandeRequest.VisaRequest visa = new CreateDemandeRequest.VisaRequest();
        visa.setLieu(demande.getLieu());
        visa.setDateEntree(demande.getDateEntree());
        visa.setDateExpiration(demande.getDateExpiration());
        visa.setMotifDemande(demande.getMotifDemande());
        request.setVisa(visa);

        return request;
    }

    @Override
    public List<DemandeListResponse> searchDemandes(
        String statut,
        Long idCategorieVisa,
        Long idNationalite,
        LocalDate dateDebut,
        LocalDate dateFin,
        String recherche
    ) {
        List<DemandeSearchProjection> results = demandeRepository.searchDemandes(
            statut,
            idCategorieVisa,
            idNationalite,
            dateDebut,
            dateFin,
            recherche
        );

        return results
            .stream()
            .map(item ->
                DemandeListResponse
                    .builder()
                    .id(item.getId())
                    .nom(item.getNom())
                    .prenom(item.getPrenom())
                    .contact(item.getContact())
                    .adresse(item.getAdresse())
                    .idCategorieVisa(item.getIdCategorieVisa())
                    .dateDemande(item.getDateDemande())
                    .lieu(item.getLieu())
                    .dateEntree(item.getDateEntree())
                    .dateExpiration(item.getDateExpiration())
                    .motifDemande(item.getMotifDemande())
                    .statut(item.getStatut())
                    .numeroPassport(item.getNumeroPassport())
                    .build()
            )
            .collect(Collectors.toList());
    }

    private void validateRequiredDocuments(Long idCategorieVisa, List<Long> uploadedDocumentIds) {
        Set<Long> uploaded = new HashSet<>(uploadedDocumentIds);
        Set<Long> required = new HashSet<>(
            documentCategorieVisaRepository.findRequiredDocumentIdsByCategorieVisa(idCategorieVisa)
        );

        if (!uploaded.containsAll(required)) {
            Set<Long> missingDocuments = new HashSet<>(required);
            missingDocuments.removeAll(uploaded);
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "Pieces obligatoires manquantes: " + missingDocuments
            );
        }
    }
}
