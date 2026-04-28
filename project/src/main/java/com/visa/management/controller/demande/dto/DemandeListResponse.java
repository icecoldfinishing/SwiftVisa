package com.visa.management.controller.demande.dto;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class DemandeListResponse {

    private Long id;
    private String nom;
    private String prenom;
    private String contact;
    private String adresse;
    private Long idCategorieVisa;
    private String categorieVisaLibelle;
    private String demandeTypeLibelle;
    private Long idDemandeDonneesType;
    private String demandeDonneesLibelle;
    private String nationaliteLibelle;
    private Long idDemandeSource;
    private LocalDate dateDemande;
    private String lieu;
    private LocalDate dateEntree;
    private LocalDate dateExpiration;
    private String motifDemande;
    private String statut;
    private String numeroPassport;
}
