package com.visa.management.repository.demande;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.visa.management.model.demande.DemandeDocumentScan;

public interface DemandeDocumentScanRepository extends JpaRepository<DemandeDocumentScan, Long> {

    Optional<DemandeDocumentScan> findByIdDemandeDocument(Long idDemandeDocument);

    List<DemandeDocumentScan> findByIdDemandeDocumentIn(Collection<Long> idDemandeDocuments);
}
