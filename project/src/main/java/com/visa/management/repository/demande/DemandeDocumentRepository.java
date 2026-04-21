package com.visa.management.repository.demande;

import com.visa.management.model.demande.DemandeDocument;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DemandeDocumentRepository extends JpaRepository<DemandeDocument, Long> {
}
