package com.visa.management.model.documents;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.time.LocalDateTime;

@Entity
@Table(
    name = "document_categorie_visa",
    uniqueConstraints = {
        @UniqueConstraint(name = "uq_document_categorie_visa", columnNames = {"id_document", "id_categorie_visa"})
    }
)
public class DocumentCategorieVisa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "id_document")
    private Long idDocument;

    @Column(name = "id_categorie_visa")
    private Long idCategorieVisa;

    @Column(name = "is_obligatoire", nullable = false)
    private boolean obligatoire;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getIdDocument() {
        return idDocument;
    }

    public void setIdDocument(Long idDocument) {
        this.idDocument = idDocument;
    }

    public Long getIdCategorieVisa() {
        return idCategorieVisa;
    }

    public void setIdCategorieVisa(Long idCategorieVisa) {
        this.idCategorieVisa = idCategorieVisa;
    }

    public boolean isObligatoire() {
        return obligatoire;
    }

    public void setObligatoire(boolean obligatoire) {
        this.obligatoire = obligatoire;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
