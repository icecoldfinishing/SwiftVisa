package com.visa.management.repository.demandeur;

import com.visa.management.model.demandeur.Demandeur;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DemandeurRepository extends JpaRepository<Demandeur, Long> {
}
