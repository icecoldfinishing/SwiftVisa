package com.visa.management.service.demande;

import com.visa.management.controller.demande.dto.CreateDemandeRequest;
import com.visa.management.controller.demande.dto.DemandeCreatedResponse;
import com.visa.management.controller.demande.dto.DemandeListResponse;
import com.visa.management.model.demande.Demande;
import com.visa.management.model.demande.DemandeDocument;
import com.visa.management.model.demande.DemandeReference;
import com.visa.management.model.demande.DemandeReferenceType;
import com.visa.management.model.demande.DemandeStatus;
import com.visa.management.model.demande.DemandeType;
import com.visa.management.model.demande.DemandeTypeDonnees;
import com.visa.management.model.demande.DemandeTypeStatus;
import com.visa.management.model.demandeur.Demandeur;
import com.visa.management.model.passport.Passport;
import com.visa.management.model.visa.CarteResidant;
import com.visa.management.model.visa.Visa;
import com.visa.management.repository.demande.DemandeDocumentRepository;
import com.visa.management.repository.demande.DemandeRepository;
import com.visa.management.repository.demande.DemandeStatusRepository;
import com.visa.management.repository.demande.DemandeReferenceRepository;
import com.visa.management.repository.demande.DemandeReferenceTypeRepository;
import com.visa.management.repository.demande.DemandeTypeDonneesRepository;
import com.visa.management.repository.demande.DemandeTypeRepository;
import com.visa.management.repository.demande.DemandeTypeStatusRepository;
import com.visa.management.repository.demande.projection.DemandeSearchProjection;
import com.visa.management.repository.documents.DocumentCategorieVisaRepository;
import com.visa.management.repository.demandeur.DemandeurRepository;
import com.visa.management.repository.passport.PassportRepository;
import com.visa.management.repository.visa.CarteResidantRepository;
import com.visa.management.repository.visa.VisaRepository;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class DemandeServiceImpl implements DemandeService {

    private static final String DOSSIER_CREE = "DOSSIER_CREE";
    private static final String DOSSIER_VALIDE = "DOSSIER_VALIDE";
    private static final String TYPE_NOUVEAU_TITRE = "NOUVEAU_TITRE";
    private static final String TYPE_DUPLICATA_CARTE = "DUPLICATAT_CARTE_RESIDENCE";
    private static final String TYPE_TRANSFERT_VISA = "TRANSFERT_VISA_NOUVEAU_PASSEPORT";
    private static final String DONNEES_AVEC = "AVEC_DONNEES_ANTERIEUR";
    private static final String DONNEES_SANS = "SANS_DONNEES_ANTERIEUR";
    private static final String REF_PASSPORT = "PASSPORT_ANTERIEUR";
    private static final String REF_VISA = "VISA_ANTERIEUR";
    private static final String REF_CARTE = "CARTE_RESIDANT_ANTERIEUR";

    private final DemandeRepository demandeRepository;
    private final DemandeurRepository demandeurRepository;
    private final PassportRepository passportRepository;
    private final DemandeTypeStatusRepository demandeTypeStatusRepository;
    private final DemandeStatusRepository demandeStatusRepository;
    private final DocumentCategorieVisaRepository documentCategorieVisaRepository;
    private final DemandeDocumentRepository demandeDocumentRepository;
    private final DemandeTypeRepository demandeTypeRepository;
    private final DemandeTypeDonneesRepository demandeTypeDonneesRepository;
    private final DemandeReferenceTypeRepository demandeReferenceTypeRepository;
    private final DemandeReferenceRepository demandeReferenceRepository;
    private final VisaRepository visaRepository;
    private final CarteResidantRepository carteResidantRepository;

    public DemandeServiceImpl(
        DemandeRepository demandeRepository,
        DemandeurRepository demandeurRepository,
        PassportRepository passportRepository,
        DemandeTypeStatusRepository demandeTypeStatusRepository,
        DemandeStatusRepository demandeStatusRepository,
        DocumentCategorieVisaRepository documentCategorieVisaRepository,
        DemandeDocumentRepository demandeDocumentRepository,
        DemandeTypeRepository demandeTypeRepository,
        DemandeTypeDonneesRepository demandeTypeDonneesRepository,
        DemandeReferenceTypeRepository demandeReferenceTypeRepository,
        DemandeReferenceRepository demandeReferenceRepository,
        VisaRepository visaRepository,
        CarteResidantRepository carteResidantRepository
    ) {
        this.demandeRepository = demandeRepository;
        this.demandeurRepository = demandeurRepository;
        this.passportRepository = passportRepository;
        this.demandeTypeStatusRepository = demandeTypeStatusRepository;
        this.demandeStatusRepository = demandeStatusRepository;
        this.documentCategorieVisaRepository = documentCategorieVisaRepository;
        this.demandeDocumentRepository = demandeDocumentRepository;
        this.demandeTypeRepository = demandeTypeRepository;
        this.demandeTypeDonneesRepository = demandeTypeDonneesRepository;
        this.demandeReferenceTypeRepository = demandeReferenceTypeRepository;
        this.demandeReferenceRepository = demandeReferenceRepository;
        this.visaRepository = visaRepository;
        this.carteResidantRepository = carteResidantRepository;
    }

    @Override
    public List<Demande> findAllDemandes() {
        return demandeRepository.findAll();
    }

    @Override
    @Transactional
    public DemandeCreatedResponse validateDemande(Long idDemande) {
        Demande demande = demandeRepository
            .findById(idDemande)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Dossier introuvable"));

        DemandeType demandeType = resolveDemandeType(demande.getIdDemandeType());
        Long validationTargetId = idDemande;
        if (demande.getIdDemandeSource() != null && isSpecialType(demandeType.getCode())) {
            validationTargetId = demande.getIdDemandeSource();
        }
        setDemandeStatus(validationTargetId, DOSSIER_VALIDE, "Dossier valide");

        if (TYPE_DUPLICATA_CARTE.equalsIgnoreCase(demandeType.getCode())) {
            ensureCarteResidant(demande);
        } else if (TYPE_TRANSFERT_VISA.equalsIgnoreCase(demandeType.getCode())) {
            ensureVisa(demande);
        }

        return new DemandeCreatedResponse(idDemande, DOSSIER_VALIDE);
    }

    @Override
    @Transactional
    public DemandeCreatedResponse createDemande(CreateDemandeRequest request) {
        validateRequiredDocuments(request.getIdCategorieVisa(), request.getUploadedDocumentIds());

        DemandeType demandeType = resolveDemandeType(request.getIdDemandeType());
        DemandeTypeDonnees donneesType = resolveDemandeTypeDonnees(request.getIdDemandeDonneesType());
        String demandeTypeCode = demandeType.getCode();
        String donneesTypeCode = donneesType != null ? donneesType.getCode() : null;
        boolean isSpecial = isSpecialType(demandeTypeCode);

        if (isSpecial && donneesType == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Type de donnees requis pour ce type de demande.");
        }

        if (isSpecial && DONNEES_AVEC.equalsIgnoreCase(donneesTypeCode)) {
            validateReferenceData(request);
        }

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

        if (isSpecial && DONNEES_SANS.equalsIgnoreCase(donneesTypeCode)) {
            DemandeType nouveauTitre = demandeTypeRepository
                .findByCodeIgnoreCase(TYPE_NOUVEAU_TITRE)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Type nouveau titre introuvable"));

            Demande baseDemande = buildDemande(savedDemandeur.getId(), savedPassport.getId(), nouveauTitre.getId(), null, request);
            Demande savedBaseDemande = demandeRepository.save(baseDemande);
            setDemandeStatus(savedBaseDemande.getId(), DOSSIER_VALIDE, "Dossier valide");

            Demande demande = buildDemande(
                savedDemandeur.getId(),
                savedPassport.getId(),
                demandeType.getId(),
                request.getIdDemandeDonneesType(),
                request
            );
            demande.setIdDemandeSource(savedBaseDemande.getId());
            Demande savedDemande = demandeRepository.save(demande);

            saveDemandeDocuments(savedBaseDemande.getId(), request.getUploadedDocumentIds());
            saveDemandeDocuments(savedDemande.getId(), request.getUploadedDocumentIds());
            createDemandeStatus(savedDemande.getId());

            return new DemandeCreatedResponse(savedDemande.getId(), DOSSIER_CREE);
        }

        Demande demande = buildDemande(
            savedDemandeur.getId(),
            savedPassport.getId(),
            demandeType.getId(),
            isSpecial ? request.getIdDemandeDonneesType() : null,
            request
        );
        Demande savedDemande = demandeRepository.save(demande);

        saveDemandeDocuments(savedDemande.getId(), request.getUploadedDocumentIds());
        createDemandeStatus(savedDemande.getId());

        if (isSpecial && DONNEES_AVEC.equalsIgnoreCase(donneesTypeCode)) {
            saveDemandeReferences(savedDemande.getId(), request);
        }

        return new DemandeCreatedResponse(savedDemande.getId(), DOSSIER_CREE);
    }

    @Override
    @Transactional
    public DemandeCreatedResponse updateDemande(Long idDemande, CreateDemandeRequest request) {
        validateRequiredDocuments(request.getIdCategorieVisa(), request.getUploadedDocumentIds());

        DemandeType demandeType = resolveDemandeType(request.getIdDemandeType());
        DemandeTypeDonnees donneesType = resolveDemandeTypeDonnees(request.getIdDemandeDonneesType());
        String demandeTypeCode = demandeType.getCode();
        String donneesTypeCode = donneesType != null ? donneesType.getCode() : null;
        boolean isSpecial = isSpecialType(demandeTypeCode);

        if (isSpecial && donneesType == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Type de donnees requis pour ce type de demande.");
        }

        if (isSpecial && DONNEES_AVEC.equalsIgnoreCase(donneesTypeCode)) {
            validateReferenceData(request);
        }

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

        demande.setIdDemandeType(demandeType.getId());
        demande.setIdCategorieVisa(request.getIdCategorieVisa());
        demande.setIdDemandeDonneesType(isSpecial ? request.getIdDemandeDonneesType() : null);
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

        demandeReferenceRepository.deleteAllByIdDemande(idDemande);
        if (isSpecial && DONNEES_AVEC.equalsIgnoreCase(donneesTypeCode)) {
            saveDemandeReferences(idDemande, request);
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
        request.setIdDemandeType(demande.getIdDemandeType());
        request.setIdCategorieVisa(demande.getIdCategorieVisa());
        request.setIdDemandeDonneesType(demande.getIdDemandeDonneesType());
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

        applyReferenceData(idDemande, request);

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
                    .categorieVisaLibelle(item.getCategorieVisaLibelle())
                    .demandeTypeLibelle(item.getDemandeTypeLibelle())
                    .idDemandeDonneesType(item.getIdDemandeDonneesType())
                    .demandeDonneesLibelle(item.getDemandeDonneesLibelle())
                    .nationaliteLibelle(item.getNationaliteLibelle())
                    .idDemandeSource(item.getIdDemandeSource())
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

    private DemandeType resolveDemandeType(Long idDemandeType) {
        if (idDemandeType == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Type de demande requis.");
        }
        return demandeTypeRepository
            .findById(idDemandeType)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Type de demande introuvable."));
    }

    private DemandeTypeDonnees resolveDemandeTypeDonnees(Long idDemandeDonneesType) {
        if (idDemandeDonneesType == null) {
            return null;
        }
        return demandeTypeDonneesRepository
            .findById(idDemandeDonneesType)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Type de donnees introuvable."));
    }

    private boolean isSpecialType(String demandeTypeCode) {
        if (demandeTypeCode == null) {
            return false;
        }
        return TYPE_DUPLICATA_CARTE.equalsIgnoreCase(demandeTypeCode)
            || TYPE_TRANSFERT_VISA.equalsIgnoreCase(demandeTypeCode);
    }

    private Demande buildDemande(
        Long idDemandeur,
        Long idPassport,
        Long idDemandeType,
        Long idDemandeDonneesType,
        CreateDemandeRequest request
    ) {
        Demande demande = new Demande();
        demande.setIdDemandeur(idDemandeur);
        demande.setIdDemandeType(idDemandeType);
        demande.setIdCategorieVisa(request.getIdCategorieVisa());
        demande.setIdDemandeDonneesType(idDemandeDonneesType);
        demande.setIdPassport(idPassport);
        demande.setDateDemande(LocalDate.now());
        demande.setLieu(request.getVisa().getLieu());
        demande.setDateEntree(request.getVisa().getDateEntree());
        demande.setDateExpiration(request.getVisa().getDateExpiration());
        demande.setMotifDemande(request.getVisa().getMotifDemande());
        return demande;
    }

    private void saveDemandeDocuments(Long idDemande, List<Long> uploadedDocumentIds) {
        Set<Long> uploadedDocuments = new HashSet<>(uploadedDocumentIds);
        for (Long documentId : uploadedDocuments) {
            DemandeDocument demandeDocument = new DemandeDocument();
            demandeDocument.setIdDemande(idDemande);
            demandeDocument.setIdDocument(documentId);
            demandeDocumentRepository.save(demandeDocument);
        }
    }

    private void createDemandeStatus(Long idDemande) {
        setDemandeStatus(idDemande, DOSSIER_CREE, "Dossier cree");
    }

    private void setDemandeStatus(Long idDemande, String statusCode, String libelle) {
        DemandeTypeStatus statusType = demandeTypeStatusRepository
            .findByCodeIgnoreCase(statusCode)
            .orElseGet(() -> {
                DemandeTypeStatus status = new DemandeTypeStatus();
                status.setCode(statusCode);
                status.setLibelle(libelle);
                return demandeTypeStatusRepository.save(status);
            });

        DemandeStatus demandeStatus = demandeStatusRepository
            .findTopByIdDemandeOrderByIdDesc(idDemande)
            .orElseGet(DemandeStatus::new);
        demandeStatus.setIdDemande(idDemande);
        demandeStatus.setIdDemandeTypeStatus(statusType.getId());
        demandeStatusRepository.save(demandeStatus);
    }

    private void ensureVisa(Demande demande) {
        if (visaRepository.findByIdDemande(demande.getId()).isPresent()) {
            return;
        }
        Visa visa = new Visa();
        visa.setIdDemande(demande.getId());
        visa.setDateDebut(demande.getDateEntree());
        visa.setDateFin(demande.getDateExpiration());
        Visa savedVisa = visaRepository.save(visa);
        savedVisa.setRef(formatRef("V", savedVisa.getId()));
        visaRepository.save(savedVisa);
    }

    private void ensureCarteResidant(Demande demande) {
        if (carteResidantRepository.findByIdDemande(demande.getId()).isPresent()) {
            return;
        }
        CarteResidant carte = new CarteResidant();
        carte.setIdDemande(demande.getId());
        carte.setDateDebut(demande.getDateEntree());
        carte.setDateFin(demande.getDateExpiration());
        CarteResidant savedCarte = carteResidantRepository.save(carte);
        savedCarte.setRef(formatRef("CR", savedCarte.getId()));
        carteResidantRepository.save(savedCarte);
    }

    private String formatRef(String prefix, Long id) {
        long safeId = id != null ? id : 0L;
        return String.format("%s%04d", prefix, safeId);
    }

    private void saveDemandeReferences(Long idDemande, CreateDemandeRequest request) {
        saveReference(idDemande, REF_PASSPORT, request.getNumeroPassportAnterieur());
        saveReference(idDemande, REF_VISA, request.getRefVisaAnterieur());
        saveReference(idDemande, REF_CARTE, request.getRefCarteResidantAnterieur());
    }

    private void saveReference(Long idDemande, String typeCode, String value) {
        if (isBlank(value)) {
            return;
        }
        Long typeId = demandeReferenceTypeRepository
            .findByCodeIgnoreCase(typeCode)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Type de reference introuvable."))
            .getId();

        DemandeReference reference = new DemandeReference();
        reference.setIdDemande(idDemande);
        reference.setIdDemandeReferenceType(typeId);
        reference.setValeur(value.trim());
        demandeReferenceRepository.save(reference);
    }

    private void validateReferenceData(CreateDemandeRequest request) {
        if (
            isBlank(request.getNumeroPassportAnterieur())
                && isBlank(request.getRefVisaAnterieur())
                && isBlank(request.getRefCarteResidantAnterieur())
        ) {
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "Au moins une reference anterieure est requise."
            );
        }
    }

    private void applyReferenceData(Long idDemande, CreateDemandeRequest request) {
        List<DemandeReference> references = demandeReferenceRepository.findByIdDemande(idDemande);
        if (references.isEmpty()) {
            return;
        }

        Set<Long> typeIds = references
            .stream()
            .map(DemandeReference::getIdDemandeReferenceType)
            .collect(Collectors.toSet());

        Map<Long, String> typeCodes = demandeReferenceTypeRepository
            .findAllById(typeIds)
            .stream()
            .collect(Collectors.toMap(DemandeReferenceType::getId, DemandeReferenceType::getCode));

        for (DemandeReference reference : references) {
            String code = typeCodes.get(reference.getIdDemandeReferenceType());
            if (REF_PASSPORT.equalsIgnoreCase(code)) {
                request.setNumeroPassportAnterieur(reference.getValeur());
            } else if (REF_VISA.equalsIgnoreCase(code)) {
                request.setRefVisaAnterieur(reference.getValeur());
            } else if (REF_CARTE.equalsIgnoreCase(code)) {
                request.setRefCarteResidantAnterieur(reference.getValeur());
            }
        }
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }
}
