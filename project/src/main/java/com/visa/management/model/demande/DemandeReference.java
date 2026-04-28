package com.visa.management.model.demande;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "demande_reference")
public class DemandeReference {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "id_demande", nullable = false)
    private Long idDemande;

    @Column(name = "id_demande_reference_type", nullable = false)
    private Long idDemandeReferenceType;

    @Column(name = "valeur", length = 100, nullable = false)
    private String valeur;

    @Column(name = "created_at")
    private LocalDateTime createdAt;
}
