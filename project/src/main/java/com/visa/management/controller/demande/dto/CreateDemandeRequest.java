package com.visa.management.controller.demande.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;
import lombok.Data;

@Data
public class CreateDemandeRequest {

    @NotNull
    private Long idDemandeType;

    @NotNull
    private Long idCategorieVisa;

    private Long idDemandeDonneesType;

    private String numeroPassportAnterieur;

    private String refVisaAnterieur;

    private String refCarteResidantAnterieur;

    @Valid
    @NotNull
    private EtatCivilRequest etatCivil;

    @Valid
    @NotNull
    private PassportRequest passport;

    @Valid
    @NotNull
    private VisaRequest visa;

    @NotEmpty
    private List<Long> uploadedDocumentIds;

    @Data
    public static class EtatCivilRequest {

        @NotBlank
        private String nom;

        @NotBlank
        private String prenom;

        private String nomJeuneFille;

        @NotNull
        private LocalDate dateNaissance;

        @NotBlank
        private String lieuNaissance;

        @NotNull
        private Long situationFamiliale;

        @NotNull
        private Long nationalite;

        @NotBlank
        private String contact;

        @NotBlank
        private String adresse;
    }

    @Data
    public static class PassportRequest {

        @NotBlank
        private String numero;

        @NotNull
        private LocalDate dateDelivrance;

        @NotNull
        private LocalDate dateExpiration;
    }

    @Data
    public static class VisaRequest {

        @NotBlank
        private String lieu;

        @NotNull
        private LocalDate dateEntree;

        @NotNull
        private LocalDate dateExpiration;

        @NotBlank
        private String motifDemande;
    }
}
