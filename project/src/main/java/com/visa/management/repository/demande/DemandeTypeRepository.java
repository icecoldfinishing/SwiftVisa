package com.visa.management.repository.demande;

import com.visa.management.model.demande.DemandeType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DemandeTypeRepository extends JpaRepository<DemandeType, Long> {
}
