package com.visa.management.controller.demande.view;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.visa.management.controller.demande.dto.CreateDemandeRequest;
import com.visa.management.controller.demande.dto.DemandeListResponse;
import com.visa.management.model.documents.DocumentCategorieVisa;
import com.visa.management.service.demande.DemandeService;
import com.visa.management.service.demande.DemandeTypeDonneesService;
import com.visa.management.service.demande.DemandeTypeService;
import com.visa.management.service.documents.DocumentCategorieVisaService;
import com.visa.management.service.documents.DocumentService;
import com.visa.management.service.utilities.NationaliteService;
import com.visa.management.service.utilities.SituationFamilialeService;
import com.visa.management.service.visa.VisaCategorieService;

@Controller
public class DossierViewController {

    private final DemandeService demandeService;
    private final SituationFamilialeService situationFamilialeService;
    private final NationaliteService nationaliteService;
    private final VisaCategorieService visaCategorieService;
    private final DemandeTypeService demandeTypeService;
    private final DemandeTypeDonneesService demandeTypeDonneesService;
    private final DocumentService documentService;
    private final DocumentCategorieVisaService documentCategorieVisaService;

    public DossierViewController(
        DemandeService demandeService,
        SituationFamilialeService situationFamilialeService,
        NationaliteService nationaliteService,
        VisaCategorieService visaCategorieService,
        DemandeTypeService demandeTypeService,
        DemandeTypeDonneesService demandeTypeDonneesService,
        DocumentService documentService,
        DocumentCategorieVisaService documentCategorieVisaService
    ) {
        this.demandeService = demandeService;
        this.situationFamilialeService = situationFamilialeService;
        this.nationaliteService = nationaliteService;
        this.visaCategorieService = visaCategorieService;
        this.demandeTypeService = demandeTypeService;
        this.demandeTypeDonneesService = demandeTypeDonneesService;
        this.documentService = documentService;
        this.documentCategorieVisaService = documentCategorieVisaService;
    }

    @GetMapping("/dossiers/nouveau")
    public String newDossier(Model model) {
        addLayoutContext(model, "dossiers-new");
        DossierForm dossierForm;
        if (!model.containsAttribute("dossierForm")) {
            dossierForm = new DossierForm();
            model.addAttribute("dossierForm", dossierForm);
        } else {
            dossierForm = (DossierForm) model.getAttribute("dossierForm");
        }
        applyFormDateDefaults(dossierForm);
        addDossierFormOptions(model);
        return "dossier/form";
    }

    @GetMapping("/dossiers/{id}/modifier")
    public String editDossier(@PathVariable Long id, Model model) {
        addLayoutContext(model, "dossiers");
        if (!model.containsAttribute("dossierForm")) {
            CreateDemandeRequest request = demandeService.getDemandeForEdit(id);
            model.addAttribute("dossierForm", fromRequest(request));
        }
        model.addAttribute("isEditMode", true);
        model.addAttribute("dossierId", id);
        addDossierFormOptions(model);
        return "dossier/form";
    }

    @PostMapping("/dossiers")
    public String createDossier(@ModelAttribute DossierForm form, RedirectAttributes redirectAttributes) {
        try {
            demandeService.createDemande(toRequest(form));
            redirectAttributes.addFlashAttribute("successMessage", "Dossier cree avec succes.");
            return "redirect:/dossiers";
        } catch (Exception exception) {
            redirectAttributes.addFlashAttribute("errorMessage", exception.getMessage());
            redirectAttributes.addFlashAttribute("dossierForm", form);
            return "redirect:/dossiers/nouveau";
        }
    }

    @PostMapping("/dossiers/{id}")
    public String updateDossier(@PathVariable Long id, @ModelAttribute DossierForm form, RedirectAttributes redirectAttributes) {
        try {
            demandeService.updateDemande(id, toRequest(form));
            redirectAttributes.addFlashAttribute("successMessage", "Dossier modifie avec succes.");
            return "redirect:/dossiers";
        } catch (Exception exception) {
            redirectAttributes.addFlashAttribute("errorMessage", exception.getMessage());
            redirectAttributes.addFlashAttribute("dossierForm", form);
            return "redirect:/dossiers/" + id + "/modifier";
        }
    }

    @PostMapping("/dossiers/{id}/valider")
    public String validateDossier(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            demandeService.validateDemande(id);
            redirectAttributes.addFlashAttribute("successMessage", "Dossier valide avec succes.");
        } catch (Exception exception) {
            redirectAttributes.addFlashAttribute("errorMessage", exception.getMessage());
        }
        return "redirect:/dossiers";
    }

    @GetMapping("/dossiers/{id}/scanner")
    public String scanDossier(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        try {
            addLayoutContext(model, "queue");
            model.addAttribute("dossierId", id);
            model.addAttribute("scanDocuments", demandeService.getDemandeDocumentsForScan(id));
            return "dossier/scan";
        } catch (Exception exception) {
            redirectAttributes.addFlashAttribute("errorMessage", exception.getMessage());
            return "redirect:/dossiers";
        }
    }

    @PostMapping("/dossiers/{id}/scanner/{idDemandeDocument}")
    public String uploadScan(
        @PathVariable Long id,
        @PathVariable Long idDemandeDocument,
        @RequestParam("scanFile") MultipartFile scanFile,
        RedirectAttributes redirectAttributes
    ) {
        try {
            demandeService.uploadDemandeDocumentScan(id, idDemandeDocument, scanFile);
            redirectAttributes.addFlashAttribute("successMessage", "Scan enregistre avec succes.");
        } catch (Exception exception) {
            redirectAttributes.addFlashAttribute("errorMessage", exception.getMessage());
        }
        return "redirect:/dossiers/" + id + "/scanner";
    }

    @PostMapping("/dossiers/{id}/scanner/terminer")
    public String finishScan(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            demandeService.finishDemandeScan(id);
            redirectAttributes.addFlashAttribute("successMessage", "Scan termine. Statut passe a DOCUMENT_SCANNER.");
            return "redirect:/dossiers";
        } catch (Exception exception) {
            redirectAttributes.addFlashAttribute("errorMessage", exception.getMessage());
            return "redirect:/dossiers/" + id + "/scanner";
        }
    }

    @GetMapping("/dossiers")
    public String listDossiers(
        @RequestParam(required = false) String statut,
        @RequestParam(required = false) Long idCategorieVisa,
        @RequestParam(required = false) Long idNationalite,
        @RequestParam(required = false) LocalDate dateDebut,
        @RequestParam(required = false) LocalDate dateFin,
        @RequestParam(required = false) String recherche,
        Model model
    ) {
        addLayoutContext(model, "dossiers");
        List<DemandeListResponse> dossiers = demandeService.searchDemandes(
            statut,
            idCategorieVisa,
            idNationalite,
            dateDebut,
            dateFin,
            recherche
        );
        model.addAttribute("dossiers", dossiers);
        model.addAttribute("visaCategories", visaCategorieService.findAllVisaCategories());
        model.addAttribute("nationalites", nationaliteService.findAllNationalites());
        model.addAttribute("statut", statut);
        model.addAttribute("idCategorieVisa", idCategorieVisa);
        model.addAttribute("idNationalite", idNationalite);
        model.addAttribute("dateDebut", dateDebut);
        model.addAttribute("dateFin", dateFin);
        model.addAttribute("recherche", recherche);
        return "dossier/list";
    }

    private CreateDemandeRequest toRequest(DossierForm form) {
        CreateDemandeRequest request = new CreateDemandeRequest();
        request.setIdDemandeType(form.getIdDemandeType());
        request.setIdCategorieVisa(form.getIdCategorieVisa());
        request.setIdDemandeDonneesType(form.getIdDemandeDonneesType());
        request.setNumeroPassportAnterieur(form.getNumeroPassportAnterieur());
        request.setRefVisaAnterieur(form.getRefVisaAnterieur());
        request.setRefCarteResidantAnterieur(form.getRefCarteResidantAnterieur());
        request.setUploadedDocumentIds(form.getUploadedDocumentIds());

        CreateDemandeRequest.EtatCivilRequest etatCivil = new CreateDemandeRequest.EtatCivilRequest();
        etatCivil.setNom(form.getNom());
        etatCivil.setPrenom(form.getPrenom());
        etatCivil.setNomJeuneFille(form.getNomJeuneFille());
        etatCivil.setDateNaissance(form.getDateNaissance());
        etatCivil.setLieuNaissance(form.getLieuNaissance());
        etatCivil.setSituationFamiliale(form.getSituationFamiliale());
        etatCivil.setNationalite(form.getNationalite());
        etatCivil.setContact(form.getContact());
        etatCivil.setAdresse(form.getAdresse());
        request.setEtatCivil(etatCivil);

        CreateDemandeRequest.PassportRequest passport = new CreateDemandeRequest.PassportRequest();
        passport.setNumero(form.getNumeroPassport());
        passport.setDateDelivrance(form.getDateDelivrancePassport());
        passport.setDateExpiration(form.getDateExpirationPassport());
        request.setPassport(passport);

        CreateDemandeRequest.VisaRequest visa = new CreateDemandeRequest.VisaRequest();
        visa.setLieu(form.getLieuVisa());
        visa.setDateEntree(form.getDateEntreeVisa());
        visa.setDateExpiration(form.getDateExpirationVisa());
        visa.setMotifDemande(form.getMotifDemande());
        request.setVisa(visa);

        return request;
    }

    private DossierForm fromRequest(CreateDemandeRequest request) {
        DossierForm form = new DossierForm();
        form.setIdDemandeType(request.getIdDemandeType());
        form.setIdCategorieVisa(request.getIdCategorieVisa());
        form.setIdDemandeDonneesType(request.getIdDemandeDonneesType());
        form.setNumeroPassportAnterieur(request.getNumeroPassportAnterieur());
        form.setRefVisaAnterieur(request.getRefVisaAnterieur());
        form.setRefCarteResidantAnterieur(request.getRefCarteResidantAnterieur());
        form.setUploadedDocumentIds(request.getUploadedDocumentIds());

        if (request.getEtatCivil() != null) {
            form.setNom(request.getEtatCivil().getNom());
            form.setPrenom(request.getEtatCivil().getPrenom());
            form.setNomJeuneFille(request.getEtatCivil().getNomJeuneFille());
            form.setDateNaissance(request.getEtatCivil().getDateNaissance());
            form.setLieuNaissance(request.getEtatCivil().getLieuNaissance());
            form.setSituationFamiliale(request.getEtatCivil().getSituationFamiliale());
            form.setNationalite(request.getEtatCivil().getNationalite());
            form.setContact(request.getEtatCivil().getContact());
            form.setAdresse(request.getEtatCivil().getAdresse());
        }

        if (request.getPassport() != null) {
            form.setNumeroPassport(request.getPassport().getNumero());
            form.setDateDelivrancePassport(request.getPassport().getDateDelivrance());
            form.setDateExpirationPassport(request.getPassport().getDateExpiration());
        }

        if (request.getVisa() != null) {
            form.setLieuVisa(request.getVisa().getLieu());
            form.setDateEntreeVisa(request.getVisa().getDateEntree());
            form.setDateExpirationVisa(request.getVisa().getDateExpiration());
            form.setMotifDemande(request.getVisa().getMotifDemande());
        }

        return form;
    }

    private void addDossierFormOptions(Model model) {
        model.addAttribute("situationsFamiliales", situationFamilialeService.findAllSituationsFamiliales());
        model.addAttribute("nationalites", nationaliteService.findAllNationalites());
        model.addAttribute("demandeTypes", demandeTypeService.findAllDemandeTypes());
        model.addAttribute("visaCategories", visaCategorieService.findAllVisaCategories());
        model.addAttribute("demandeTypeDonnees", demandeTypeDonneesService.findAllDemandeTypeDonnees());
        model.addAttribute("documents", documentService.findAllDocuments());

        List<DocumentCategorieVisa> mappings = documentCategorieVisaService.findAllDocumentCategorieVisas();
        Map<Long, Set<Long>> requiredByCategorie = mappings
            .stream()
            .filter(DocumentCategorieVisa::isObligatoire)
            .collect(
                Collectors.groupingBy(
                    DocumentCategorieVisa::getIdCategorieVisa,
                    Collectors.mapping(DocumentCategorieVisa::getIdDocument, Collectors.toSet())
                )
            );
        Map<Long, Set<Long>> availableCategoriesByDocument = mappings
            .stream()
            .collect(
                Collectors.groupingBy(
                    DocumentCategorieVisa::getIdDocument,
                    Collectors.mapping(DocumentCategorieVisa::getIdCategorieVisa, Collectors.toSet())
                )
            );
        Map<Long, Set<Long>> requiredCategoriesByDocument = mappings
            .stream()
            .filter(DocumentCategorieVisa::isObligatoire)
            .collect(
                Collectors.groupingBy(
                    DocumentCategorieVisa::getIdDocument,
                    Collectors.mapping(DocumentCategorieVisa::getIdCategorieVisa, Collectors.toSet())
                )
            );
        model.addAttribute("requiredDocumentsByCategorie", requiredByCategorie);
        model.addAttribute("availableCategoriesByDocument", availableCategoriesByDocument);
        model.addAttribute("requiredCategoriesByDocument", requiredCategoriesByDocument);
    }

    private void addLayoutContext(Model model, String activeMenu) {
        model.addAttribute("accountName", "Compte SwiftVisa");
        model.addAttribute("accountRole", "AGENT");
        model.addAttribute("activeMenu", activeMenu);
    }

    private void applyFormDateDefaults(DossierForm form) {
        if (form.getDateNaissance() == null) {
            form.setDateNaissance(LocalDate.of(2000, 1, 1));
        }
        if (form.getDateEntreeVisa() == null) {
            form.setDateEntreeVisa(LocalDate.now());
        }
        if (form.getDateExpirationVisa() == null) {
            form.setDateExpirationVisa(LocalDate.now());
        }
        if (form.getDateDelivrancePassport() == null) {
            form.setDateDelivrancePassport(LocalDate.now());
        }
        if (form.getDateExpirationPassport() == null) {
            form.setDateExpirationPassport(LocalDate.now());
        }
    }
}
