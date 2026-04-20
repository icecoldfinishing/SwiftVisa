package com.visa.management.model.demande;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "demande")
public class Demande {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "id_demande_type")
    private Long idDemandeType;

    @Column(name = "id_demandeur")
    private Long idDemandeur;

    @Column(name = "id_categorie_visa")
    private Long idCategorieVisa;

    @Column(name = "id_passport")
    private Long idPassport;

    @Column(name = "date_demande")
    private LocalDate dateDemande;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getIdDemandeType() {
        return idDemandeType;
    }

    public void setIdDemandeType(Long idDemandeType) {
        this.idDemandeType = idDemandeType;
    }

    public Long getIdDemandeur() {
        return idDemandeur;
    }

    public void setIdDemandeur(Long idDemandeur) {
        this.idDemandeur = idDemandeur;
    }

    public Long getIdCategorieVisa() {
        return idCategorieVisa;
    }

    public void setIdCategorieVisa(Long idCategorieVisa) {
        this.idCategorieVisa = idCategorieVisa;
    }

    public Long getIdPassport() {
        return idPassport;
    }

    public void setIdPassport(Long idPassport) {
        this.idPassport = idPassport;
    }

    public LocalDate getDateDemande() {
        return dateDemande;
    }

    public void setDateDemande(LocalDate dateDemande) {
        this.dateDemande = dateDemande;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
