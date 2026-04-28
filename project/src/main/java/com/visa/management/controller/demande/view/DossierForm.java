package com.visa.management.controller.demande.view;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

@Data
public class DossierForm {

    private Long idDemandeType;
    private Long idCategorieVisa;
    private Long idDemandeDonneesType;

    private String numeroPassportAnterieur;
    private String refVisaAnterieur;
    private String refCarteResidantAnterieur;

    private String nom;
    private String prenom;
    private String nomJeuneFille;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate dateNaissance;
    private String lieuNaissance;
    private Long situationFamiliale;
    private Long nationalite;
    private String contact;
    private String adresse;

    private String numeroPassport;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate dateDelivrancePassport;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate dateExpirationPassport;

    private String lieuVisa;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate dateEntreeVisa;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate dateExpirationVisa;
    private String motifDemande;

    private List<Long> uploadedDocumentIds = new ArrayList<>();
}
