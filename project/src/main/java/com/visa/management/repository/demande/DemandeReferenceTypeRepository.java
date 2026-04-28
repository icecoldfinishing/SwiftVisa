package com.visa.management.repository.demande;

import com.visa.management.model.demande.DemandeReferenceType;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DemandeReferenceTypeRepository extends JpaRepository<DemandeReferenceType, Long> {

    Optional<DemandeReferenceType> findByCodeIgnoreCase(String code);
}
