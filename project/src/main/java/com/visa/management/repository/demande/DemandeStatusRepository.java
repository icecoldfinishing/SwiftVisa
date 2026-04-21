package com.visa.management.repository.demande;

import com.visa.management.model.demande.DemandeStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DemandeStatusRepository extends JpaRepository<DemandeStatus, Long> {
}
