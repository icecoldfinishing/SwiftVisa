package com.visa.management.repository.demande.projection;

import java.time.LocalDate;

public interface DemandeSearchProjection {

    Long getId();

    String getNom();

    String getPrenom();

    String getContact();

    String getAdresse();

    Long getIdCategorieVisa();

    String getCategorieVisaLibelle();

    String getDemandeTypeLibelle();

    Long getIdDemandeDonneesType();

    String getDemandeDonneesLibelle();

    String getNationaliteLibelle();

    Long getIdDemandeSource();

    LocalDate getDateDemande();

    String getLieu();

    LocalDate getDateEntree();

    LocalDate getDateExpiration();

    String getMotifDemande();

    String getStatut();

    String getNumeroPassport();
}
