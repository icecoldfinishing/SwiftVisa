package com.visa.management.repository.demande;

import com.visa.management.model.demande.Demande;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DemandeRepository extends JpaRepository<Demande, Long> {
}
